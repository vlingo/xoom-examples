// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.workoffload1;

import java.io.File;
import java.time.Duration;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Stoppable;
import io.vlingo.common.Cancellable;
import io.vlingo.common.Scheduled;

/**
 * Check the stability of a file by repeatedly reading its file size twice
 * within a time gap and if the sizes are the same, report it
 * as stable.
 */
public class FileStabilityBySizeCheckerActor extends Actor implements FileStabilityChecker, Scheduled<FileSizeStability> {
  private Cancellable cancellable;
  private boolean completed;
  private final Duration interval;
  private final int retries;
  private final Scheduled<FileSizeStability> scheduled;

  @SuppressWarnings("unchecked")
  public FileStabilityBySizeCheckerActor(final Duration interval, final int retries) {
    this.interval = interval;
    this.retries = retries;
    this.scheduled = selfAs(Scheduled.class);
  }

  /**
   * @see io.vlingo.workoffload1.FileStabilityChecker#determineStability(java.lang.String, io.vlingo.workoffload1.FileStabilityInterest)
   */
  @Override
  public void determineStability(final String path, final FileStabilityInterest interest) {
    determineFor(new FileSizeStability(path, interest, retries));
  }

  /**
   * @see io.vlingo.common.Scheduled#intervalSignal(io.vlingo.common.Scheduled, java.lang.Object)
   */
  @Override
  public void intervalSignal(final Scheduled<FileSizeStability> scheduled, final FileSizeStability stability) {
    cancellable.cancel();
    determineFor(stability);
  }

  //=============================
  // internal implementation
  //=============================

  private void completed() {
    completed = true;
    selfAs(Stoppable.class).conclude();
  }

  private void determineFor(final FileSizeStability stability) {
    if (completed) return;

    final FileSizeStability current = determineFor(stability, new File(stability.path));

    switch (current.status) {
    case Stable:
      stable(current);
      break;
    case Unstable:
      redetermineFor(current);
      break;
    default:
      informError(current);
      break;
    }
  }

  private FileSizeStability determineFor(final FileSizeStability stability, final File file) {
    long fileSize = FileSizeStability.MissingFile;

    if (file.isFile()) {
      fileSize = file.length();

      if (stability.isStablePer(fileSize)) {
        return stability.with(fileSize);
      }
    }

    return stability.with(fileSize);
  }

  private void informError(final FileSizeStability stability) {
    stability.interest.inform(stability.path, stability.errorResult());
    completed();
  }

  private void redetermineFor(final FileSizeStability stability) {
    cancellable = scheduler().scheduleOnce(scheduled, stability, Duration.ZERO, interval);
  }

  private void stable(final FileSizeStability stability) {
    stability.interest.inform(stability.path, ResultType.Stable);
    completed();
  }
}
