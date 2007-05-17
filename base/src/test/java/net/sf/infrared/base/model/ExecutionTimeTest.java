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
package net.sf.infrared.base.model;

import junit.framework.TestCase;

public class ExecutionTimeTest extends TestCase {

    public void testIsStarted() {
        ExecutionTimer et = new ExecutionTimer();

        assertFalse("Newly created ExecutionTime should not be in executing state", et
                .isExecuting());

        et.start();
        assertTrue("Once start() is called ExecutionTime should be in executing state", et
                .isExecuting());

        et.stop();
        assertFalse("Once stop is called ExecutionTime should not be in executing state", et
                .isExecuting());
    }

    /*
     * These defensive checks have been recoded as JDK1.4-asserts
     * So these tests dont work anymore
     * @TODO Find out how to run tests for the asserts
    public void testStartStart() {
        ExecutionTimer et = new ExecutionTimer();

        et.start();
        try {
            et.start();
            fail("Back-to-back starts should have failed");
        } catch (Throwable th) {
            assertEquals("The Exception expected is IllegalStateException",
                    IllegalStateException.class, th.getClass());
        }
    }

    public void testStartStopStart() {
        ExecutionTimer et = new ExecutionTimer();

        et.start();
        et.stop();
        try {
            et.start();
            fail("Attempting to start() and already executed ExecutionTime should have failed");
        } catch (Throwable th) {
            assertEquals("The Exception expected is IllegalStateException",
                    IllegalStateException.class, th.getClass());
        }
    }

    public void testStopAheadOfStart() {
        ExecutionTimer et = new ExecutionTimer();
        try {
            et.stop();
            fail("Attempting to call stop on a newly created ExecutionTime should have failed");
        } catch (Throwable th) {
            assertEquals("The Exception expected is IllegalStateException",
                    IllegalStateException.class, th.getClass());
        }
    }

    public void testStartStopStop() {
        ExecutionTimer et = new ExecutionTimer();

        et.start();
        et.stop();
        try {
            et.stop();
            fail("Attempting to stop() and already executed ExecutionTime should have failed");
        } catch (Throwable th) {
            assertEquals("The Exception expected is IllegalStateException",
                    IllegalStateException.class, th.getClass());
        }
    }*/

    public void testStartAndEndTimes() {
        ExecutionTimer et = new ExecutionTimer() {
            private int i = 1;

            public long getCurrentTime() {
                return 100 * i++;
            }
        };

        et.start();
        et.stop();

        assertEquals("", 100, et.getInclusiveTime());

        /*
        try {
            et.setExclusiveTime(101);
            fail("Attempting to set exclusive time greater than inclusive time should fail");
        } catch (Throwable th) {
            assertEquals("The Exception expected is IllegalArgumentException",
                    IllegalArgumentException.class, th.getClass());
        }*/

        try {
            et.setExclusiveTime(99);
        } catch (Throwable th) {
            fail("No exception expected when setting exclusive time properly");
        }
    }
}
