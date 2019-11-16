// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.rest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.http.Response;
import io.vlingo.http.ResponseParser;
import io.vlingo.wire.channel.ResponseChannelConsumer;
import io.vlingo.wire.message.ConsumerByteBuffer;

public class TestResponseChannelConsumer extends Actor implements ResponseChannelConsumer {
  private ResponseParser parser;
  private final Progress progress;
  
  public TestResponseChannelConsumer(final Progress progress) {
    this.progress = progress;
  }

  @Override
  public void consume(final ConsumerByteBuffer buffer) {
    if (parser == null) {
      parser = ResponseParser.parserFor(buffer.asByteBuffer());
    } else {
      parser.parseNext(buffer.asByteBuffer());
    }
    buffer.release();

    while (parser.hasFullResponse()) {
      final Response response = parser.fullResponse();
      progress.consumeCalls.writeUsing("consume", response);
    }
  }

  public static class Progress {
    private AccessSafely consumeCalls = AccessSafely.afterCompleting(0);

    public Queue<Response> responses = new ConcurrentLinkedQueue<>();
    public AtomicInteger consumeCount = new AtomicInteger(0);

    /**
     * Answer with an AccessSafely which writes responses to "consume" and reads the write count from "completed".
     * <p>
     * Note: Clients can replace the default lambdas with their own via readingWith/writingWith.
     * 
     * @param n Number of times consume(response) must be called before readFrom(...) will return.
     * @return
     */
    public AccessSafely expectConsumeTimes(final int n) {
      consumeCalls = AccessSafely.afterCompleting(n)
          .writingWith("consume", response -> {
            responses.add((Response) response);
            consumeCount.incrementAndGet();
          })
          .readingWith("completed", () -> consumeCalls.totalWrites())
          ;
      return consumeCalls;
    }
  }

}
