/*
 * Copyright 2005 Tavant Technologies and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Original Author:  binil.thomas (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent.util;

import junit.framework.TestCase;

public class MutableIntegerTest extends TestCase {
    public void testIfComparisonWithNullReturnsFalse() {
        MutableInteger fixture = new MutableInteger(1);
        assertFalse("mi.equals(null) should be false", fixture.equals(null));
    }

    public void testIfComparisonWithItselfReturnsTrue() {
        MutableInteger fixture = new MutableInteger(1);
        assertTrue("mi.equals(mi) should be true", fixture.equals(fixture));
    }

    public void testIfComparisonWithAnotherObjectWithTheEqualContentReturnsTrue() {
        MutableInteger fixture1 = new MutableInteger(1);
        MutableInteger fixture2 = new MutableInteger(1);
        assertTrue("mi1.equals(mi2) [where m1 and m2 contain the equal int values] " +
                "should be true", fixture1.equals(fixture2));
        assertTrue("mi2.equals(mi1) [where m1 and m2 contain the equal int values] " +
                "should be true", fixture2.equals(fixture1));
    }

    public void testIfComparisonWithAnotherObjectWithTheUnequalContentReturnsTrue() {
        MutableInteger fixture1 = new MutableInteger(1);
        MutableInteger fixture2 = new MutableInteger(2);
        assertFalse("mi1.equals(mi2) [where m1 and m2 contain the unequal int values] " +
                "should be false", fixture1.equals(fixture2));
        assertFalse("mi2.equals(mi1) [where m1 and m2 contain the unequal int values] " +
                "should be false", fixture2.equals(fixture1));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MutableIntegerTest.class);
    }
}
