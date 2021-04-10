// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.workoffload1;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.CompletesEventually;
import io.vlingo.xoom.common.Completes;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Process stable files.
 */
public class FileProcessorActor extends Actor implements FileProcessor, FileStabilityInterest {
  private FileStabilityInterest interest;
  private final Duration recheckInterval;
  private final int retries;
  private Map<String,CompletesEventually> tracking;

  public FileProcessorActor(final Duration recheckInterval, final int retries) {
    this.interest = selfAs(FileStabilityInterest.class);
    this.tracking = new HashMap<>();
    this.recheckInterval = recheckInterval;
    this.retries = retries;
  }

  @Override
  public Completes<ResultType> process(String path) {
    tracking.put(path, completesEventually());
    FileStabilityChecker.bySize(stage(), recheckInterval, retries).determineStability(path, interest);
    return completes();
  }

  /**
   * @see io.vlingo.xoom.workoffload1.FileStabilityInterest#inform(java.lang.String, io.vlingo.xoom.workoffload1.ResultType)
   */
  @Override
  public void inform(final String path, final ResultType type) {
    switch (type) {
    case Stable:
      completeWith(path, "Processed: " + path, type);
      new File(path).delete();
      break;
    case AttemptsExhausted:
      completeWith(path, "Failed due to exhausted attempts for: " + path, type);
      break;
    case NonExisting:
      completeWith(path, "Failed due to non-existing: " + path, type);
      break;
    case UnexpectedRemoval:
      completeWith(path, "Failed due to removed: " + path, type);
      break;
    case UnknownFileError:
    default:
      completeWith(path, "Failed due to unknown error: " + path, type);
      break;
    }
  }

  private void completeWith(final String path, final String message, final ResultType result) {
    final CompletesEventually completesEventually = tracking.remove(path);

    completesEventually.with(result);

    logger().info(message);
  }
}
