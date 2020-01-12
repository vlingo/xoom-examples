// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.benchmark.actors.mailbox.arrayqueue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.World;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class ArrayQueueBenchmark {
  private static final int MaxCount = 100_000_000;

  private int countTaken;
  private World world;

  // used only for test; to run:
  // $ mvn clean package
  // $ java -jar target/benchmarks.jar ArrayQueueBenchmark 
  public static void main(final String[] args) {
    new ArrayQueueBenchmark().throughputBenchmark();
  }

  @Setup
  public void setup() {
    world = World.startWithDefaults("ArrayQueueBenchmark");
  }

  @TearDown
  public void teardown() {
    if (countTaken >= MaxCount) {
      System.out.println("ArrayQueueBenchmark#throughputBenchmark ended successfully.");
    } else {
      System.out.println("ArrayQueueBenchmark#throughputBenchmark ended with unexpected results.");
    }

    world.terminate();
  }

  @Benchmark
  public void throughputBenchmark() {
    final TestResults testResults = new TestResults(MaxCount);

    final CountTaker countTaker =
            world.actorFor(
                    CountTaker.class,
                    Definition.has(CountTakerActor.class, Definition.parameters(testResults), "arrayQueueMailbox", "countTaker-1"));

    for (int count = 1; count <= MaxCount; ++count) {
      countTaker.take(count);
    }

    countTaken = testResults.waitForExpectedMessages();
  }

  //=================================
  // Protocol and Actor
  //=================================

  public static interface CountTaker {
    void take(final int count);
  }

  public static class CountTakerActor extends Actor implements CountTaker {
    private final TestResults testResults;

    public CountTakerActor(final TestResults testResults) {
      this.testResults = testResults;
    }

    @Override
    public void take(final int count) {
      testResults.increment();
    }
  }

  public static class TestResults {
    private int count;
    private final int expectedMessages;
    private final CountDownLatch latch;
    private final Object lock;

    public TestResults(final int expectedMessages) {
      this.expectedMessages = expectedMessages;
      this.latch = new CountDownLatch(1);
      this.lock = new Object();
    }

    public void increment() {
      if (++count >= expectedMessages) {
        synchronized (lock) {
          ++count; // force sync across threads
          latch.countDown();
        }
      }
    }

    public int waitForExpectedMessages() {
      int retries = 0;

      while (true) {
        try {
          latch.await();
          synchronized (lock) {
            return count;
          }
        } catch (InterruptedException e) {
          if (retries++ >= 10) {
            System.err.println("Abort benchmark.");
            System.exit(2);
          }
          // loop and wait again
        }
      }
    }
  }
}
