// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.workoffload1;

import java.time.Duration;

import io.vlingo.actors.Stage;

/**
 * Check the stability of a file according the the implementor's policy.
 */
public interface FileStabilityChecker {
  /**
   * Answer a new {@code FileStabilityChecker} that checks by stable file size.
   * @param stage the Stage in which to create the actor
   * @param interval the Duration between stability checks
   * @return FileStabilityChecker
   */
  public static FileStabilityChecker bySize(final Stage stage, final Duration interval, final int retries) {
    return stage.actorFor(FileStabilityChecker.class, FileStabilityBySizeCheckerActor.class, interval, retries);
  }

  /**
   * Determine the stability of the file identified by {@code path}.
   * @param path the String path of the file to check
   * @param interest the FileStabilityInterest to inform when the file is stable
   */
  void determineStability(final String path, final FileStabilityInterest interest);
}
