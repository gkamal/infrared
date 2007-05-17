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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Holds statistics of various layers, the time spend in those layers and the aggregate time of
 * each execution in those layers.
 * 
 * @author binil.thomas
 */
public class LayerTimeRepository implements Serializable, Cloneable {
    // map of String hierarchical layer name -> LayerTime objects
    private Map hierarchicalLayerTimes = new HashMap();

    // map of hierarchical layer name -> [Map of ExecutionContext -> AggregateExecutionTime objects]
    // This identifies aggregate timings of executions that happenend within a hierarchical layer
    private Map hierarchicalAggExecTimes = new HashMap();
        
    /**
     * Gets the hierarchical layers that executed in this application
     */
    public String[] getHierarchicalLayers() {
        Set ret = new HashSet();
        ret.addAll(hierarchicalLayerTimes.keySet());
        ret.addAll(hierarchicalAggExecTimes.keySet());
        return (String[]) ret.toArray(new String[ret.size()]);
    }
    
    /**
     * Gets the absolute layers that executed in this application
     */
    public String[] getAbsoluteLayers() {
        // @TODO cache this calculation somewhere
        String[] hLayers = getHierarchicalLayers();
        Set aLayers = new HashSet();        
        for (int i = 0; i < hLayers.length; i++) {
            String hLayer = hLayers[i];
            aLayers.addAll(parseAbsoluteLayers(hLayer));
        }
        
        return (String[]) aLayers.toArray(new String[aLayers.size()]);
    }
    
    /**
     * Gets the time spend in an hierarchical layer
     */
    public long getTimeInHierarchicalLayer(String hLayer) {
        return getLayerTimeFromMap(hierarchicalLayerTimes, hLayer);
    }

    public long getTimeInAbsoluteLayer(String aLayer) {
        String dotPrefixedLayer = "." + aLayer;
        long time = 0;
        String[] hLayers = getHierarchicalLayers();
        for (int i = 0; i < hLayers.length; i++) {
            String hLayer = hLayers[i];
            if (hLayer.equals(aLayer) || (hLayer.endsWith(dotPrefixedLayer))) {
                time += getTimeInHierarchicalLayer(hLayer);
            }
        }
        return time;
    }
    
    /**
     * Gets the aggregate of executions in that happenend in a given layer
     */
    public AggregateExecutionTime[] getExecutionsInHierarchicalLayer(String hLayer) {
        return getExecutionsFromMap(hierarchicalAggExecTimes, hLayer);
    }
    
    public AggregateExecutionTime[] getExecutionsInAbsoluteLayer(String aLayer) {
        Map ctxToAggExecs = new HashMap();
        
        String[] hLayers = getHierarchicalLayers();
        for (int i = 0; i < hLayers.length; i++) {
            String hLayer = hLayers[i];
            AggregateExecutionTime[] aets = getExecutionsInHierarchicalLayer(hLayer);
            for (int j = 0; j < aets.length; j++) {
                AggregateExecutionTime aet = aets[j];
                ExecutionContext ctx = aet.getContext();   
                if (! ctx.getLayer().equals(aLayer)) {
                    continue;
                }
                AggregateExecutionTime merged = (AggregateExecutionTime) ctxToAggExecs.get(ctx);
                if (merged == null) {
                    merged = new AggregateExecutionTime(ctx);
                    ctxToAggExecs.put(ctx, merged);
                }
                merged.merge(aet);
            }
        }
        
        return (AggregateExecutionTime[]) ctxToAggExecs.values().toArray(new AggregateExecutionTime[0]);
    }
    
    /**
     * Record that the specified time was spend in the specified hierarchical layer
     */
    public void mergeHierarchicalLayerTime(String layerName, long time) {        
        getAggregateHierarchicalLayerTime(layerName).addToTime(time);        
    }
    
    /**
     * Record that some time was spend in a layer, where the later name and time are
     * given as a LayerTime object
     */
    public void mergeHierarchicalLayerTime(LayerTime lt) {
        mergeHierarchicalLayerTime(lt.getLayer(), lt.getTime());
    }
    
    /**
     * Record that a specified list of executions happenend in the specified hierarchical layer.
     * Each execution is denoted as an ExecutionTimer object
     */
    public void mergeExecutionTimes(String hLayer, ExecutionTimer[] times) {
        for (int i = 0; i < times.length; i++) {
            ExecutionTimer et = times[i];
            getAggregateExecutionTime(hLayer, et.getContext()).merge(et); // hierarchical
        }
    }
    
    /**
     * Record that a specified list of executions happenend in the specified hierarchical layer
     * Each type of execution is denoted as an AggregateExecutionTime object
     */
    public void mergeExecutionTimes(String layerName, AggregateExecutionTime[] times) {
        for (int i = 0; i < times.length; i++) {
            AggregateExecutionTime aet = times[i];
            getAggregateExecutionTime(layerName, aet.getContext()).merge(aet); // hierarchical            
        }
    }
    
    LayerTime getAggregateHierarchicalLayerTime(String layer) {
        LayerTime lt = (LayerTime) hierarchicalLayerTimes.get(layer);
        if (lt == null) {
            lt = new LayerTime(layer);
            hierarchicalLayerTimes.put(layer, lt);
        }
        return lt;
    }
    
    AggregateExecutionTime getAggregateExecutionTime(String hLayer, ExecutionContext ctx) {
        Map m = (Map) hierarchicalAggExecTimes.get(hLayer);
        if (m == null) {
            m = new HashMap();
            hierarchicalAggExecTimes.put(hLayer, m);
            AggregateExecutionTime aet = new AggregateExecutionTime(ctx);
            m.put(ctx, aet);
            return aet;
        }
        
        AggregateExecutionTime aet = (AggregateExecutionTime) m.get(ctx);
        if (aet == null) {
            aet = new AggregateExecutionTime(ctx);
            m.put(ctx, aet);
        }
        return aet;
    }    
    
    long getLayerTimeFromMap(Map map, String l) {
        LayerTime lt = (LayerTime) map.get(l);
        if (lt != null) {
            return lt.getTime();
        } else {
            return 0;
        }
    }
    
    AggregateExecutionTime[] getExecutionsFromMap(Map map, String l) {
        Map innerMap = (Map) map.get(l);
        if (innerMap == null) {
            return new AggregateExecutionTime[0];
        }
        return (AggregateExecutionTime[]) innerMap.values().toArray(new AggregateExecutionTime[0]);
    }
    
    Collection parseAbsoluteLayers(String hLayer) {
        Set absLayers = new HashSet();

        Pattern p = Pattern.compile("[\\w]+[\\.]");
        Matcher m = p.matcher(hLayer);
        int i = 0;
        while (m.find(i)) {
            i = m.end();
            String aLayer = m.group();
            aLayer = aLayer.substring(0, aLayer.length() - 1);
            absLayers.add(aLayer);
        }
        absLayers.add(hLayer.substring(i));
        return absLayers;
    }
}
