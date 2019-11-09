// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model;

public class Tenant {
    public final String id;

    public static Tenant fromExisting(final String referencedId) {
        return new Tenant(referencedId);
    }

    public static Tenant with(final String id) {
        return new Tenant(id);
    }

    public Tenant(final String id) {
        assert (id != null);
        this.id = id;
    }
}
