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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds the performance statistics of multiple applications
 *
 * @author binil.thomas
 */
public class StatisticsSnapshot implements Serializable, Cloneable {    
    private static final int DEFAULT_MAX_LAST_INVOCATIONS = 5;
    
    private static final String SEPERATOR = " - on - ";
    
    // set of application instances for which the stats is held in this object.
    // each element would be of the form 'application - on - host'
    private Set applicationNames = new HashSet();

    // aggregated call tree for all applications
    private AggregateOperationTree tree = new AggregateOperationTree();
 
    private LayerTimeRepository repository = new LayerTimeRepository();

    // true when the PerformanceStatistics contains any real statistics
//    private boolean hasStatistics = false;

    // number of latest operation trees to store
    private int maxLastInvocations = DEFAULT_MAX_LAST_INVOCATIONS;

    // List of Tree objects. The value of a TreeNode in the tree is an ExecutionTimer object
    private LinkedList lastInvocationsList = new LinkedList();
    
    // the data held by this object is from this system time
    private long startTime = Long.MAX_VALUE;
    
    // the data held by this object is until this system time
    private long endTime = Long.MIN_VALUE;
    
    
   public synchronized void merge(ApplicationStatistics stats) {
        if (stats == null) {
            return;
        }
        applicationNames.add(getApplicationName(stats));
        
        mergeStartAndEndTimes(stats);
        mergeLayerAndExecutionTimes(stats);
        
        AggregateOperationTree tree = stats.getTree();
        mergeTree(tree);        
        addToLastInvocations(stats.getLastInvocations());        
    }
    
    public List getLastInvocations() {
        return Collections.unmodifiableList(lastInvocationsList);
    }
    
    /**
     * Sets the number of actual invocation trees stored
     */
    public void setMaxLastInvocations(int max) {
        maxLastInvocations = max;
    }

    public AggregateOperationTree getTree() {
        return tree;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
        
    public String[] getHierarchicalLayers() {
        return repository.getHierarchicalLayers();
    }
    
    public String[] getAbsoluteLayers() {
        return repository.getAbsoluteLayers();
    }
    
    public long getTimeInHierarchicalLayer(String hLayer) {
        return repository.getTimeInHierarchicalLayer(hLayer);
    }
    
    public long getTimeInAbsoluteLayer(String aLayer) {
        return repository.getTimeInAbsoluteLayer(aLayer);
    }
    
    public AggregateExecutionTime[] getExecutionsInHierarchicalLayer(String hLayer) {
        return repository.getExecutionsInHierarchicalLayer(hLayer);
    }
    
    public AggregateExecutionTime[] getExecutionsInAbsoluteLayer(String aLayer) {
        return repository.getExecutionsInAbsoluteLayer(aLayer);
    }
   
    public Set getApplicationNames() {
        return Collections.unmodifiableSet(applicationNames);
    }
    
    public static StatisticsSnapshot createSnapshot(Collection appNames, 
            Collection instanceIds, Map layerTimes, Map executionTimes, AggregateOperationTree tree, 
            long startTime, long endTime) {
        StatisticsSnapshot stats = new StatisticsSnapshot();
        for (Iterator i = layerTimes.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String layer = (String) entry.getKey();
            LayerTime lt = (LayerTime) entry.getValue();
            stats.mergeLayerTime(layer, lt.getTime());
        }
        for (Iterator i = executionTimes.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String layer = (String) entry.getKey();
            List executions = (List) entry.getValue();
            stats.mergeExecutionTimes(layer, 
                    (AggregateExecutionTime[]) executions.toArray(new AggregateExecutionTime[0]));
        }
        stats.tree = tree;
        stats.startTime = startTime;
        stats.endTime = endTime;
        stats.applicationNames = new HashSet(appNames);
        return stats;
    }
    
    String getApplicationName(ApplicationStatistics stats) {
        return stats.getApplicationName() + SEPERATOR + stats.getInstanceId();
    }
    
    void mergeTree(AggregateOperationTree tree) {
        if (tree != null) {
            this.tree.merge(tree);
//            hasStatistics = true;
        }
    }
    
    void mergeLayerAndExecutionTimes(ApplicationStatistics stats) {
        String[] layers = stats.getLayers();
        for (int i = 0; i < layers.length; i++) {
            String aLayer = layers[i];
            mergeLayerTime(aLayer, stats.getTimeInLayer(aLayer));
            mergeExecutionTimes(aLayer, stats.getExecutionsInLayer(aLayer));
        }
    }
    
    void mergeLayerTime(String layerName, long time) {        
        repository.mergeHierarchicalLayerTime(layerName, time);
//        hasStatistics = true;
    }
    
    void mergeExecutionTimes(String layerName, AggregateExecutionTime[] times) {
        repository.mergeExecutionTimes(layerName, times);
//        hasStatistics = true;        
    }
        
    // @TODO: improve considering the clocks & time zones of the agents
    void mergeStartAndEndTimes(ApplicationStatistics stats) {
        startTime = Math.min(startTime, stats.getStartTime());
        endTime = Math.max(endTime, stats.getEndTime());
    }
    
    // @TODO: 'last' here is definied as that merged later, which might not be correct
    void addToLastInvocations(List trees) {
        if ( (trees == null) || (trees.isEmpty()) ) {
            return;
        }
        
        lastInvocationsList.addAll(trees);
        int size = lastInvocationsList.size();
        
        if (size > maxLastInvocations) {
            for (int i = size; i > maxLastInvocations; i--) {
                lastInvocationsList.removeFirst();
            }
        }
                
//        hasStatistics = true;
    }
}
