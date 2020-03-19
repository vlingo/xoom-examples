// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.calculation.domain;

public class CalculationId {

    public final String value;

    public CalculationId(final String value) {
        this.value = value;
    }

    public static CalculationId from(final String value) {
        return new CalculationId(value);
    }

}
