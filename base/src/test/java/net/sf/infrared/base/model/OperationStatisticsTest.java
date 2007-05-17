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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

import org.easymock.MockControl;

/**
 *
 * @author binil.thomas
 */
public class OperationStatisticsTest extends TestCase {
    
    public OperationStatisticsTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(OperationStatisticsTest.class);
        
        return suite;
    }

    public void testSetOperationTree() {
        TreeNode node = TreeNode.createTreeNode("test-node");
        Tree tree = new Tree(node);
        
        OperationStatistics fixture = new OperationStatistics("test-app", "test-instance");
        
        try {
            fixture.setOperationTree(tree);
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
            fail();
        }
        
        assertSame(tree, fixture.getOperationTree());
        
        TreeNode anotherNode = TreeNode.createTreeNode("another-test-node");
        Tree anotherTree = new Tree(anotherNode);
        try {
            fixture.setOperationTree(anotherTree);
            fail();
        } catch (IllegalStateException expected) {
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
            fail();
        }
        
        assertSame(tree, fixture.getOperationTree());
    }

    public void testSetLayerTimes() {
        OperationStatistics fixture = new OperationStatistics("test-app", "test-instance");
        assertEquals(0, fixture.getLayers().length);
         
        Map layerTimes = new HashMap();
        LayerTime lt1 = new LayerTime("test-layer");
        layerTimes.put("test-layer", lt1);
        LayerTime lt2 = new LayerTime("another-test-layer");
        layerTimes.put("another-test-layer", lt2);
        
        
        fixture.setLayerTimes(layerTimes);
        
        assertEquals(2, fixture.getLayers().length);
    }

    public void testSetExecutionTimes() {
        OperationStatistics fixture = new OperationStatistics("test-app", "test-instance");
        assertEquals(0, fixture.getLayers().length);
        
        Map layerTimes = new HashMap();
        LayerTime lt1 = new LayerTime("test-layer");
        layerTimes.put("test-layer", lt1);
        fixture.setLayerTimes(layerTimes);
        
        Map executionTimes = new HashMap();
        List executions1 = new ArrayList();
        executions1.add(createTimer(createMockExecutionContext(), 10));
        executions1.add(createTimer(createMockExecutionContext(), 20));
        executions1.add(createTimer(createMockExecutionContext(), 30));
        executionTimes.put("test-layer", executions1);
        
        fixture.setExecutionTimes(executionTimes);
        
        assertEquals(1, fixture.getLayers().length);
        assertEquals(3, fixture.getExecutions("test-layer").length);
    }
    
    public void testSettingDifferentLayersInSetExecutionsAndSetLayerTimes() {
        OperationStatistics fixture = new OperationStatistics("test-app", "test-instance");
        assertEquals(0, fixture.getLayers().length);
           
        Map layerTimes = new HashMap();
        LayerTime lt1 = new LayerTime("test-layer");
        layerTimes.put("test-layer", lt1);
        fixture.setLayerTimes(layerTimes);
        
        Map executionTimes = new HashMap();
        List executions1 = new ArrayList();
        executions1.add(createTimer(createMockExecutionContext(), 10));        
        executionTimes.put("another-test-layer", executions1);
        
        try {
            fixture.setExecutionTimes(executionTimes);
            fail();
        } catch (IllegalArgumentException expected) {
        } catch (Throwable th) {
        }        
        
        fixture = new OperationStatistics("test-app", "test-instance");
        fixture.setExecutionTimes(executionTimes);
        try {
            fixture.setLayerTimes(layerTimes);
            fail();
        } catch (IllegalArgumentException expected) {
        } catch (Throwable th) {
        }
    }
    
    private ExecutionContext createMockExecutionContext() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        return (ExecutionContext) ctrl.getMock();
    }
    
    private ExecutionTimer createTimer(ExecutionContext ctx, final long time) {
        ExecutionTimer et = new ExecutionTimer(ctx) {
            int i = 0; long[] times = new long[] {10, time + 10};
            public long getCurrentTime() {
                return times[i++];
            }
        };
        et.start(); // time now will be 10
        et.stop();  // time now will be time + 10
        et.setExclusiveTime(0);
        return et;
    }
    

    public void testSetEndTime() {        
        OperationStatistics fixture = new OperationStatistics("test-app", "test-instance");
        
        fixture.setEndTime(10L);
        
        try {
            fixture.setEndTime(15L);
        } catch (IllegalStateException expected) {
        } catch (Throwable th) {
        }
    }
    
    public void testSetStartTime() {        
        OperationStatistics fixture = new OperationStatistics("test-app", "test-instance");
        
        fixture.setStartTime(10L);
        
        try {
            fixture.setStartTime(15L);
        } catch (IllegalStateException expected) {
        } catch (Throwable th) {
        }
    }
}
