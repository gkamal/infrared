/*
 * LayerTimeRepositoryTest.java
 * JUnit based test
 *
 * Created on November 26, 2005, 8:10 PM
 */

package net.sf.infrared.base.model;

import java.util.Collection;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author binil.thomas
 */
public class LayerTimeRepositoryTest extends TestCase {
    
    public LayerTimeRepositoryTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LayerTimeRepositoryTest.class);
        
        return suite;
    }

    public void testMergeLayerTime(){
        LayerTimeRepository fixture = new LayerTimeRepository();
        
        LayerTime lt1 = new LayerTime("layer1");
        lt1.setTime(100);
        fixture.mergeHierarchicalLayerTime(lt1);        
        
        String[] hLayers = fixture.getHierarchicalLayers();
        assertEquals(1, hLayers.length); 
        // layer1 has 100
        assertEquals(100, fixture.getTimeInHierarchicalLayer("layer1"));
        
        String[] aLayers = fixture.getAbsoluteLayers();
        assertEquals(1, aLayers.length); 
        // layer1 has 100
        assertEquals(100, fixture.getTimeInAbsoluteLayer("layer1"));
        
        
        LayerTime lt2 = new LayerTime("layer1.layer2");
        lt2.setTime(100);
        fixture.mergeHierarchicalLayerTime(lt2);        
        hLayers = fixture.getHierarchicalLayers();
        assertEquals(2, hLayers.length);
        assertEquals(100, fixture.getTimeInHierarchicalLayer("layer1")); // layer1 should be unaffected
        // layer1.layer2 now has 100
        assertEquals(100, fixture.getTimeInHierarchicalLayer("layer1.layer2"));
        
        aLayers = fixture.getAbsoluteLayers();
        assertEquals(2, aLayers.length);        
        assertEquals(100, fixture.getTimeInAbsoluteLayer("layer1"));
        assertEquals(100, fixture.getTimeInAbsoluteLayer("layer2"));
                                                                           
        LayerTime lt3 = new LayerTime("layer1");
        lt3.setTime(100);
        fixture.mergeHierarchicalLayerTime(lt3);        
        hLayers = fixture.getHierarchicalLayers();
        assertEquals(2, hLayers.length);
        // layer1 now has 200, with the new time added
        assertEquals(200, fixture.getTimeInHierarchicalLayer("layer1"));
        // layer1.layer2 is unaffected
        assertEquals(100, fixture.getTimeInHierarchicalLayer("layer1.layer2"));
        
        aLayers = fixture.getAbsoluteLayers();
        assertEquals(2, aLayers.length);        
        assertEquals(200, fixture.getTimeInAbsoluteLayer("layer1"));
        assertEquals(100, fixture.getTimeInAbsoluteLayer("layer2"));
    }
    
    public void testMergeExecutionTimes(){
        LayerTimeRepository fixture = new LayerTimeRepository();
        
        // create some mock ExecutionContexts
        ExecutionContext a = createMockExecutionContext("layer1");
        ExecutionContext b = createMockExecutionContext("layer1");
        ExecutionContext c = createMockExecutionContext("layer1");
        ExecutionContext d = createMockExecutionContext("layer2");        
        
        // create a timer array for mocks a & b
        ExecutionTimer[] times1 = new ExecutionTimer[] { createTimer(a, 100),
                                                         createTimer(b, 100) };
        // merge it as part of layer1
        fixture.mergeExecutionTimes("layer1", times1);
        
        // test if the layer 'layer1' has these new stats added        
        assertEquals(2, fixture.getExecutionsInHierarchicalLayer("layer1").length);
        assertEquals(0, fixture.getExecutionsInHierarchicalLayer("layer1.layer2").length);
        
        assertEquals(2, fixture.getExecutionsInAbsoluteLayer("layer1").length);
        assertEquals(0, fixture.getExecutionsInAbsoluteLayer("layer2").length);
        
        // create timer array for mocks c & d
        ExecutionTimer[] times2 = new ExecutionTimer[] { createTimer(c, 100),
                                                         createTimer(d, 100) };
        // merge it as part of 'layer1.layer2'
        fixture.mergeExecutionTimes("layer1.layer2", times2);
        
        // test if the layer 'layer1.layer2' has these new stats
        // aslo test that 'layer1' is unaffected        
        assertEquals(2, fixture.getExecutionsInHierarchicalLayer("layer1").length);
        assertEquals(2, fixture.getExecutionsInHierarchicalLayer("layer1.layer2").length);
        
        assertEquals(3, fixture.getExecutionsInAbsoluteLayer("layer1").length);
        assertEquals(1, fixture.getExecutionsInAbsoluteLayer("layer2").length);
        
        // create a timer for mock c
        ExecutionTimer[] times3 = new ExecutionTimer[] { createTimer(c, 200) };
        // merge it as part of layer 'layer1'
        // remember that c is part of 'layer1.layer2' and 'layer1' already has
        // two othere executions under it
        fixture.mergeExecutionTimes("layer1", times3);
        
        // test that this new stats of c is added to 'layer1'
        // also test that 'layer1.layer2' is unaffected        
        assertEquals(3, fixture.getExecutionsInHierarchicalLayer("layer1").length);
        assertEquals(2, fixture.getExecutionsInHierarchicalLayer("layer1.layer2").length);
        
        assertEquals(3, fixture.getExecutionsInAbsoluteLayer("layer1").length);
        assertEquals(1, fixture.getExecutionsInAbsoluteLayer("layer2").length);
    }
    
    public void testParsingAbsoluteLayers() {
        LayerTimeRepository fixture = new LayerTimeRepository();
        
        Collection c = fixture.parseAbsoluteLayers("layer1");
        assertEquals(1, c.size());
        assertTrue(c.contains("layer1"));
        
        c = fixture.parseAbsoluteLayers("layer1.layer2");
        assertEquals(2, c.size());
        assertTrue(c.contains("layer1"));
        assertTrue(c.contains("layer2"));
        
        c = fixture.parseAbsoluteLayers("layer1.layer2.layer3");
        assertEquals(3, c.size());
        assertTrue(c.contains("layer1"));
        assertTrue(c.contains("layer2"));
        assertTrue(c.contains("layer3"));
    }
    
    private ExecutionContext createMockExecutionContext(String layer) {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext ctx = (ExecutionContext) ctrl.getMock();
        ctx.getLayer();
        ctrl.setDefaultReturnValue(layer);
        ctrl.replay();
        return ctx;
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
        et.setExclusiveTime(50);
        return et;
    }
}
