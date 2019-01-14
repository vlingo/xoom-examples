// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.eventjournal;

import java.util.List;
import java.util.Optional;

import io.vlingo.common.Outcome;
import io.vlingo.eventjournal.counter.Counter;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.journal.Journal.AppendResultInterest;

public class MockCounterAppendResultInterest implements AppendResultInterest<Counter> {

  @Override
  public <S> void appendResultedIn(Outcome<StorageException, Result> outcome, String streamName, int streamVersion,
          Source<S> source, Optional<Counter> snapshot, Object object) {
    
  }

  @Override
  public <S> void appendAllResultedIn(Outcome<StorageException, Result> outcome, String streamName, int streamVersion,
          List<Source<S>> sources, Optional<Counter> snapshot, Object object) {
    
  }
}
