// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.workoffload1;

/**
 * Result type for file processing.
 */
public enum ResultType {
  Stable,
  AttemptsExhausted,
  NonExisting,
  UnexpectedRemoval,
  UnknownFileError,
  Unstable;

  public boolean isError() {
    return this == AttemptsExhausted || this == NonExisting || this == UnexpectedRemoval || this == UnknownFileError;
  }

  public boolean isStable() {
    return this == Stable;
  }

  public boolean isUnstable() {
    return this == Unstable;
  }
}
