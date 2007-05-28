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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   binil.thomas;
 *
 */
package net.sf.infrared.agent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.infrared.agent.util.MutableInteger;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * 
 * @author kamal.govindraj
 * @author binil.thomas
 */
public class LayerTimeTracker {
    private static final Logger log = LoggingFactory.getLogger(LayerTimeTracker.class);

    private static final String LAYER_SEPARATOR = ".";

    private static final int LAYER_PATH_INITIAL_SIZE = 256;

    // Captures the total time taken for execution of APIs in each layer. At the
    // end of the request, this map is passed over to the statistics object, 
    // which updates the layer timings in its Map.
    // Holds String (layer name) -> LayerTime entries
    private Map layerTimings = new HashMap();

    // Determines when the control exits a layer. At this time, the execution
    // time has to be added to the layer time.
    private Map layerCount = new HashMap();

    private StringBuffer layerPath = new StringBuffer(LAYER_PATH_INITIAL_SIZE);
    
    private long pruneThreshold = -1;

    public void enterLayer(ExecutionTimer et) {
        String layer = et.getContext().getLayer();
        enterLayer(layer);
    }

    public boolean leaveLayer(ExecutionTimer et) {
        String layer = et.getContext().getLayer();
        return leaveLayer(layer, et.getInclusiveTime());
    }

    public Map reset(boolean isFaulty) {
//        if (log.isDebugEnabled()) {
//            log.debug(this + " - Resetting layer timings");
//        }
        Map oldLayerTimings = layerTimings;
        layerTimings = new HashMap();
        if (isFaulty) {
            layerCount.clear();
            layerPath = new StringBuffer(LAYER_PATH_INITIAL_SIZE);
        }
        
        return oldLayerTimings;
    }

    public Map getLayerTimings() {
        return Collections.unmodifiableMap(layerTimings);
    }

    public String toString() {
        return "LayerTimeTracker for thread " + Thread.currentThread();
    }
    
    public String getCurrentLayer() {
        return layerPath.toString().intern();
    }

    void enterLayer(String layer) {
        MutableInteger currentCount = getLayerCount(layer);

        if (currentCount.isZero()) {
            addLayerToPath(layer);
        }

        currentCount.increment();

        if (log.isDebugEnabled()) {
            log.debug(this + " - Entered layer " + layer + " for the " + currentCount + "-th time");
        }
    }

    boolean leaveLayer(String layer, long time) {
        MutableInteger currentCount = getLayerCount(layer);
        assert currentCount.isPositive(): 
            this + " - Mistmatch in Enter/Leave layer. Count should be +ve, was " + currentCount;
        

        currentCount.decrement();
        /*
        if (log.isDebugEnabled()) {
            log.debug(this + " - Leaving layer " + layer + ". " + "We are still " + currentCount
                    + " deep in this layer");
        }*/

        if (currentCount.isZero()) {
            // We are leaving the outermost method call from a layer
            // Need to add the time to the layers time
            addTimeToLayer(getCurrentLayer(), time);
            removeLayerFromPath();
        }

        return true;
    }

    long getPruneBelowTime() {
        return pruneThreshold;
    }

    void setPruneBelowTime(long time) {
        pruneThreshold = time;
    }
    
    private MutableInteger getLayerCount(String layer) {
        MutableInteger count = (MutableInteger) layerCount.get(layer);

        if (count == null) {
            count = new MutableInteger(0);
            layerCount.put(layer, count);
        }

        return count;
    }

    private void addLayerToPath(String layer) {
        if (layerPath.length() > 0) {
            layerPath.append(LAYER_SEPARATOR);
        }

        layerPath.append(layer);
    }

    private void addTimeToLayer(String layer, long executionTime) {
        if (executionTime <= getPruneBelowTime()) {
            if (log.isDebugEnabled()) {
                log.debug("Discarded tracking of layer " + layer + 
                        " because the time (" + executionTime + 
                        ") <= prune threshold (" + getPruneBelowTime() + ")");
            }
            return;
        }
        LayerTime layerTime = (LayerTime) layerTimings.get(layer);

        if (layerTime == null) {
            layerTime = new LayerTime(layer);
            layerTimings.put(layer, layerTime);
        }

        layerTime.addToTime(executionTime);

        if (log.isDebugEnabled()) {
            log.debug(this + " - Adding " + executionTime + " to layer " + layer);
        }
    }

    private void removeLayerFromPath() {
        if (layerPath.lastIndexOf(LAYER_SEPARATOR) > -1) {
            // Strip off the last part of the layer name
            layerPath.delete(layerPath.lastIndexOf(LAYER_SEPARATOR), layerPath.length());
        } else {
            layerPath.delete(0, layerPath.length());
        }
    }    
}
