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
package net.sf.infrared.base.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestCase for LoggingFactory
 *
 * @author Binil Thomas
 */
public class LoggingFactoryTest extends TestCase {
    protected void setUp() {
        System.setProperty(LoggingFactory.DEBUG_KEY, "");
        System.setProperty(LoggingFactory.INFO_KEY, "");
    }

    public void testIfInfoIsNotEnabledIfFlagIsNotSet() {
        //we set the debug flag, but not the info one
        System.setProperty(LoggingFactory.INFO_KEY, "");
        System.setProperty("infrared.debug", "true");
        assertFalse(LoggingFactory.isInfoLoggingEnabled());
        System.setProperty(LoggingFactory.DEBUG_KEY, "false");
        assertFalse(LoggingFactory.isInfoLoggingEnabled());
        // LoggingFactory.ifInfoEnabled() is dependant only
        // on the INFO flag, and not on the DEBUG flag
    }

    public void testIfInfoIsEnabledIfFlagIsSet() {
        System.setProperty(LoggingFactory.INFO_KEY, "true");
        assertTrue(LoggingFactory.isInfoLoggingEnabled());
    }

    public void testIfDebugIsNotEnabledIfFlagIsNotSet() {
        assertFalse(LoggingFactory.isDebugLoggingEnabled());
    }

    public void testIfDebugIsEnabledIfFlagIsSet() {
        System.setProperty(LoggingFactory.DEBUG_KEY, "true");
        assertTrue(LoggingFactory.isDebugLoggingEnabled());
    }

    public void testIfDebugIsEnabledIfInfoIsEnabled() {
        System.setProperty(LoggingFactory.INFO_KEY, "true");
        assertTrue(LoggingFactory.isDebugLoggingEnabled());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(LoggingFactoryTest.class));
    }
}
