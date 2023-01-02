// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.workoffload1;

import java.time.Duration;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

/**
 * Processes files by off-loading stability checking.
 */
public interface FileProcessor {
  /**
   * Answer a new FileProcessor with the given interval and retries.
   * @param stage the Stage in which to create the FileProcessor 
   * @param interval the Duration between checks
   * @param retries the int number of possible retries
   * @return FileProcessor
   */
  public static FileProcessor using(final Stage stage, final Duration interval, final int retries) {
    return stage.actorFor(FileProcessor.class, FileProcessorActor.class, interval, retries);
  }

  /**
   * Answer the eventual {@code ResultType} of processing the file identified by {@code path}.
   * @param path the String path of the file to process
   * @return {@code Completes<ResultType>}
   */
  Completes<ResultType> process(final String path);
}
