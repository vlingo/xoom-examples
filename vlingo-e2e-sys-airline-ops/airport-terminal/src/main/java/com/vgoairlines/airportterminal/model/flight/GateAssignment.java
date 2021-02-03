// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

public class GateAssignment {

    public final String terminal;
    public final String number;

    public static GateAssignment with(final String terminal, final String number) {
        return new GateAssignment(terminal, number);
    }

    private GateAssignment(final String terminal, final String number) {
        this.terminal = terminal;
        this.number = number;
    }
}
