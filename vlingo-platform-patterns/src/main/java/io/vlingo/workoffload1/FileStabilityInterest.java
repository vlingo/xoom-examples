// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.workoffload1;

/**
 * An interest in FileStabilityChecker results.
 */
public interface FileStabilityInterest {
  /**
   * Inform the receiver of the stability check results of the file identified by {@code path}.
   * @param path the String path of the file being checked
   * @param type the ResultType
   */
  void inform(final String path, final ResultType type);
}
