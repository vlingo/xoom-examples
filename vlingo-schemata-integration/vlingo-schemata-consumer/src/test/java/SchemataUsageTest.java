// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

import io.vlingo.examples.schemata.event.SchemaDefined;
import io.vlingo.examples.schemata.event.SchemaPublished;
import org.junit.Test;

public class SchemataUsageTest {

    @Test
    public void testThatSchemaIsPulledAndCompiledTest() {
        new SchemaDefined();
        new SchemaPublished();
    }
}
