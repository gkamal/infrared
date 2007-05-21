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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent;

import java.util.Map;

import junit.framework.TestCase;
import net.sf.infrared.base.model.LayerTime;

/*
 * A
 *  -- B
 *      -- C
 * B
 */
public class LayerTimeTrackerTest extends TestCase {
    public void testLayerTimeCalculation() {
        LayerTimeTracker fixture = new LayerTimeTracker();
        
        fixture.enterLayer("A");
            fixture.enterLayer("B");
                fixture.enterLayer("C");
                fixture.leaveLayer("C", 10);
            fixture.leaveLayer("B", 20);
            
            fixture.enterLayer("C");
            fixture.leaveLayer("C", 15);
        fixture.leaveLayer("A", 50);
        
        fixture.enterLayer("B");
        fixture.leaveLayer("B", 20);
        
        
        Map layerTimings = fixture.getLayerTimings();
        
        assertEquals("", 5, layerTimings.size());
        assertEquals("", 50, ( (LayerTime) layerTimings.get("A") ).getTime());
        assertEquals("", 20, ( (LayerTime) layerTimings.get("A.B") ).getTime());
        assertEquals("", 10, ( (LayerTime) layerTimings.get("A.B.C") ).getTime());
        assertEquals("", 15, ( (LayerTime) layerTimings.get("A.C") ).getTime());
        assertEquals("", 20, ( (LayerTime) layerTimings.get("B") ).getTime());
    }
    
    /*
     * A
     *  -- B
     *      -- A
     * A
     */
    public void testLayerTimeCalculation2() {
        LayerTimeTracker fixture = new LayerTimeTracker();
        
        fixture.enterLayer("A");
            fixture.enterLayer("B");
                fixture.enterLayer("A");
                fixture.leaveLayer("A", 10); // this is counted along with the ancestors time!
            fixture.leaveLayer("B", 20);
        fixture.leaveLayer("A", 50);
        
        fixture.enterLayer("A");
        fixture.leaveLayer("A", 15);
        
        Map layerTimings = fixture.getLayerTimings();
        assertEquals("", 2, layerTimings.size());
        assertEquals("", 65, ( (LayerTime) layerTimings.get("A") ).getTime());
        assertEquals("", 20, ( (LayerTime) layerTimings.get("A.B") ).getTime());
    }
}
