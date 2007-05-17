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

import org.easymock.MockControl;

import junit.framework.TestCase;

public class AggregateExecutionTimeTest extends TestCase {
    public AggregateExecutionTimeTest(String str) {
        super(str);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     @TODO: Moved the defensive check to a JDK1.4-assert. Hence this test will
     make sense only if the merges are attempted with asserts enabled.
    public void testIllegalMerge() {
        MockControl ctrl1 = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx1 = (ExecutionContext) ctrl1.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx1);

        MockControl ctrl2 = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx2 = (ExecutionContext) ctrl2.getMock();
        AggregateExecutionTime aet2 = new AggregateExecutionTime(mockCtx2);

        assertFalse("Mering an AggregateExecutionTime with another having a different context "
                + "should return false", aet1.merge(aet2));

        ExecutionTimer et = new ExecutionTimer(mockCtx2);
        assertFalse("Mering an AggregateExecutionTime with an ExecutionTime having a "
                + "different context should return false", aet1.merge(et));
    }*/

    public void testExecutionCountAfterMerge() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx);
        AggregateExecutionTime aet2 = new AggregateExecutionTime(mockCtx);
        aet1.setExecutionCount(3);
        aet2.setExecutionCount(2);

        aet1.merge(aet2);

        assertEquals("", 5, aet1.getExecutionCount());
        assertEquals("", 2, aet2.getExecutionCount());

        ExecutionTimer et = new ExecutionTimer(mockCtx);
        aet2.merge(et);

        assertEquals("", 3, aet2.getExecutionCount());
    }

    public void testTotalInclusiveTimeAfterMerge() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx);
        AggregateExecutionTime aet2 = new AggregateExecutionTime(mockCtx);

        aet1.setTotalInclusiveTime(100);
        aet2.setTotalInclusiveTime(200);

        aet1.merge(aet2);

        assertEquals("", 300, aet1.getTotalInclusiveTime());
        assertEquals("", 200, aet2.getTotalInclusiveTime());

        ExecutionTimer et = new ExecutionTimer(mockCtx);
        et.setInclusiveTime(100);
        aet2.merge(et);

        assertEquals("", 300, aet2.getTotalInclusiveTime());
    }

    public void testMaxInclusiveTimeAfterMerge() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx);
        AggregateExecutionTime aet2 = new AggregateExecutionTime(mockCtx);

        aet1.setMaxInclusiveTime(100);
        aet2.setMaxInclusiveTime(200);

        aet1.merge(aet2);

        assertEquals("", 200, aet1.getMaxInclusiveTime());
        assertEquals("", 200, aet2.getMaxInclusiveTime());

        aet1.setMaxInclusiveTime(300);
        aet2.setMaxInclusiveTime(200);

        aet1.merge(aet2);

        assertEquals("", 300, aet1.getMaxInclusiveTime());
        assertEquals("", 200, aet2.getMaxInclusiveTime());

        ExecutionTimer et = new ExecutionTimer(mockCtx);
        et.setInclusiveTime(300);
        aet2.merge(et);
        assertEquals("", 300, aet2.getMaxInclusiveTime());

        et.setInclusiveTime(200);
        aet2.merge(et);
        assertEquals("", 300, aet2.getMaxInclusiveTime());
    }

    public void testMinInclusiveTimeAfterMerge() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx);
        AggregateExecutionTime aet2 = new AggregateExecutionTime(mockCtx);

        aet1.setMinInclusiveTime(100);
        aet2.setMinInclusiveTime(200);

        aet1.merge(aet2);

        assertEquals("", 100, aet1.getMinInclusiveTime());
        assertEquals("", 200, aet2.getMinInclusiveTime());

        aet1.setMinInclusiveTime(300);
        aet2.setMinInclusiveTime(200);

        aet1.merge(aet2);

        assertEquals("", 200, aet1.getMinInclusiveTime());
        assertEquals("", 200, aet2.getMinInclusiveTime());

        ExecutionTimer et = new ExecutionTimer(mockCtx);
        et.setInclusiveTime(300);
        aet2.merge(et);
        assertEquals("", 200, aet2.getMinInclusiveTime());

        et.setInclusiveTime(100);
        aet2.merge(et);
        assertEquals("", 100, aet2.getMinInclusiveTime());
    }

    public void testTimeOfLastExecutionAfterMerge() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx);
        AggregateExecutionTime aet2 = new AggregateExecutionTime(mockCtx);

        aet1.setTimeOfLastExecution(100);
        aet2.setTimeOfLastExecution(200);

        aet1.merge(aet2);

        assertEquals("", 200, aet1.getTimeOfLastExecution());
        assertEquals("", 200, aet2.getTimeOfLastExecution());

        aet1.setTimeOfLastExecution(300);
        aet2.setTimeOfLastExecution(200);

        aet1.merge(aet2);

        assertEquals("", 300, aet1.getTimeOfLastExecution());
        assertEquals("", 200, aet2.getTimeOfLastExecution());

        ExecutionTimer et = new ExecutionTimer(mockCtx);
        et.start();
        et.stop();
        aet2.merge(et);
        assertEquals("", et.getStartTime(), aet2.getTimeOfLastExecution());

        long t = System.currentTimeMillis() + 10;
        aet2.setTimeOfLastExecution(t);
        aet2.merge(et);
        assertEquals("", t, aet2.getTimeOfLastExecution());
    }

    public void testTimeOfFirstExecutionAfterMerge() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet1 = new AggregateExecutionTime(mockCtx);
        AggregateExecutionTime evt2 = new AggregateExecutionTime(mockCtx);

        aet1.setTimeOfFirstExecution(100);
        evt2.setTimeOfFirstExecution(200);

        aet1.merge(evt2);

        assertEquals("", 100, aet1.getTimeOfFirstExecution());
        assertEquals("", 200, evt2.getTimeOfFirstExecution());

        aet1.setTimeOfFirstExecution(300);
        evt2.setTimeOfFirstExecution(200);

        aet1.merge(evt2);

        assertEquals("", 200, aet1.getTimeOfFirstExecution());
        assertEquals("", 200, evt2.getTimeOfFirstExecution());
    }

    public void testClone() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        AggregateExecutionTime aet = new AggregateExecutionTime(mockCtx);

        try {
            aet.clone();
        } catch (Throwable th) {
            fail("Cloning AggregateExecutionTime should not throw anything");
        }
    }
}
