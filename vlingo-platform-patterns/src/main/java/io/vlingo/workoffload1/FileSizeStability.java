// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.workoffload1;

/**
 * Stability measured per file size.
 */
class FileSizeStability {
  static final int NoSize = -1;
  static final int MissingFile = -2;

  final int attempts;
  final long fileSize;
  final FileStabilityInterest interest;
  final int maximumAttempts;
  final String path;
  final ResultType status;

  /**
   * Construct my default state with no {@code fileSize}.
   * @param path the String path of the file being checked
   * @param interest the FileStabilityInterest to inform when the file is stable
   * @param maximumAttempts the int maximum number of stability attempts
   */
  FileSizeStability(final String path, final FileStabilityInterest interest, final int maximumAttempts) {
    this(path, interest, ResultType.Unstable, NoSize, 0, maximumAttempts);
  }

  /**
   * Answer the error {@code ResultType} per the stability progress.
   * @return ResultType
   */
  ResultType errorResult() {
    return           noFile() ? ResultType.NonExisting :
                removedFile() ? ResultType.UnexpectedRemoval :
           exceededAttempts() ? ResultType.AttemptsExhausted :
                                ResultType.UnknownFileError;
  }

  /**
   * Answer whether or not the maximum number of attempts was reached.
   * @return boolean
   */
  boolean exceededAttempts() {
    return exceedsWith(attempts);
  }

  /**
   * Answer whether or not there may be another attempt.
   * @return boolean
   */
  boolean mayReattempt() {
    return !noFile() && !removedFile() && !exceededAttempts();
  }

  /**
   * Answer whether or not the file is considered missing.
   * @return boolean
   */
  boolean noFile() {
    return fileSize == NoSize;
  }

  /**
   * Answer whether or not the file previously existed but does not now.
   * @return boolean
   */
  boolean removedFile() {
    return fileSize == MissingFile;
  }

  /**
   * Answer whether or not stability has be reached per {@code fileSize}.
   * @param fileSize the long size of the file identified by path
   * @return boolean
   */
  boolean isStablePer(final long fileSize) {
    return this.fileSize == fileSize;
  }

  boolean isUnstable() {
    return status.isUnstable();
  }

  boolean willExceedAttempts() {
    return exceedsWith(attempts + 1);
  }

  /**
   * Answer a new {@code StabilityData} with the {@code fileSize}.
   * @param fileSize the long size of the file identified by path
   * @return StabilityData
   */
  FileSizeStability with(final long fileSize) {
    final ResultType result;

    switch ((int) fileSize) {
    case MissingFile:
      result = this.fileSize == NoSize ? ResultType.NonExisting : ResultType.UnexpectedRemoval;

      return new FileSizeStability(path, interest, result, NoSize, attempts, maximumAttempts);
    default:
      result = this.fileSize == fileSize ? ResultType.Stable :
               willExceedAttempts()      ? ResultType.AttemptsExhausted :
                                           ResultType.Unstable;

      return new FileSizeStability(path, interest, result, fileSize, attempts + 1, maximumAttempts);
    }
  }

  /**
   * Construct my default state with a {@code fileSize}.
   * @param path the String path of the file being checked
   * @param interest the FileStabilityInterest to inform when the file is stable
   * @param fileSize the long current size of the file
   * @param attempts the int total number of stability checks attempted
   * @param maximumAttempts the int maximum number of stability attempts
   */
  private FileSizeStability(final String path, final FileStabilityInterest interest, final ResultType result, final long fileSize, final int attempts, final int maximumAttempts) {
    this.path = path;
    this.interest = interest;
    this.status = result;
    this.fileSize = fileSize;
    this.attempts = attempts;
    this.maximumAttempts = maximumAttempts;
  }

  /**
   * Answer whether or not {@code attempts} exceeds the maximum.
   * @param attempts the int number of attempts to check
   * @return boolean
   */
  private boolean exceedsWith(final int attempts) {
    return attempts > maximumAttempts;
  }
}
