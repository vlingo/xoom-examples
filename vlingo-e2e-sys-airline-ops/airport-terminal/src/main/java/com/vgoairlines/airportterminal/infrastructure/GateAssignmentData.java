// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

public class GateAssignmentData {

    public final String terminal;
    public final String number;

    public GateAssignmentData(final String terminal, final String number) {
        this.terminal = terminal;
        this.number = number;
    }
}
