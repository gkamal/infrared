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
 * Original Author:  binil.thoms (Tavant Technologies)
 * Contributor(s):   prashant.nair, subin.p
 *
 */
package net.sf.infrared.base.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.infrared.base.util.Tree;

/**
 * Represents the statistics collected for an application.
 * 
 * <p>
 * Multiple threads in the application collect the statistics of individual operations, 
 * and those are merged onto an ApplicationStatistics by the aggregator in use.
 * 
 * @author binil.thomas
 * @author prashant.nair
 * @author subin.p
 */
public class ApplicationStatistics extends AbstractStatistics {
    private static final int DEFAULT_MAX_LAST_INVOCATIONS = 5;

    // aggregated call tree for the entire application
    private AggregateOperationTree tree;
     
    // true when the ApplicationStatistics contains any real statistics
    private boolean hasStatistics = false;

    // number of latest operation trees to store
    private int maxLastInvocations = DEFAULT_MAX_LAST_INVOCATIONS;

    // List of Tree objects. The value of a TreeNode in the tree is an ExecutionTimer object
    private LinkedList lastInvocationsList;
    
    // the data held by this object is from this system time
    private long startTime = Long.MAX_VALUE;
    
    // the data held by this object is until this system time
    private long endTime = Long.MIN_VALUE;
    
    private LayerTimeRepository repository;

    public ApplicationStatistics(String applicationName, String instanceId) {
        super(applicationName, instanceId);
        reset();
    }

    /**
     * Resets this application statistics
     */
    public synchronized void reset() {
        repository = new LayerTimeRepository();
        hasStatistics = false;
        tree = new AggregateOperationTree();
        lastInvocationsList = new LinkedList();
        startTime = Long.MAX_VALUE;
        endTime = Long.MIN_VALUE;
    }

    /**
     * Merges the statistics of an operation in this application on this ApplicationStatistics
     */
    public synchronized void merge(OperationStatistics opStats) {
        if (opStats == null) {
            return;
        }
        if ( (! getApplicationName().equals( opStats.getApplicationName() )) 
                || (! getInstanceId().equals( opStats.getInstanceId() )) ){
            throw new IllegalArgumentException("Incorrect application name or instanceId:" +
                    " Can't merge " + opStats + " to " + this);
        }
        
        mergeStartAndEndTimes(opStats);
        mergeLayerAndExecutionTimes(opStats);
        
        Tree tree = opStats.getOperationTree();
        addToLastInvocations(tree);
        mergeTree(tree);
    }
    
    /**
     * Merges another ApplicationStatistics onto this on
     */
    public synchronized void merge(ApplicationStatistics otherStats) {
        if (otherStats == null) {
            return;
        }
        if ( (! getApplicationName().equals( otherStats.getApplicationName() )) 
                || (! getInstanceId().equals( otherStats.getInstanceId() )) ){
            throw new IllegalArgumentException("Incorrect application name or instanceId:" +
                    " Can't merge " + otherStats + " to " + this);
        }
        
        mergeStartAndEndTimes(otherStats);
        mergeLayerAndExecutionTimes(otherStats);
        
        AggregateOperationTree tree = otherStats.getTree();
        mergeTree(tree);
        
        addToLastInvocations(otherStats.getLastInvocations());
    }

    /**
     * False if this ApplicationStatistics is empty and contains no statistics, else true
     */
    public boolean hasStatistics() {
        return hasStatistics;
    }

    public List getLastInvocations() {
        return Collections.unmodifiableList(lastInvocationsList);
    }

    public AggregateOperationTree getTree() {
        return tree;
    }

    public int getMaxLastInvocations() {
        return maxLastInvocations;
    }

    /**
     * Sets the number of actual invocation trees stored
     */
    public void setMaxLastInvocations(int max) {
        maxLastInvocations = max;
    }
    
    /**
     * Gets the layers that executed in this application
     */
    public String[] getLayers() {
        return repository.getHierarchicalLayers();
    }
    
    /**
     * Gets the time spend in a layer
     */
    public long getTimeInLayer(String layer) {
        return repository.getTimeInHierarchicalLayer(layer);
    }
    
    /**
     * Gets the aggregate of executions in that happenend in a given layer
     */
    public AggregateExecutionTime[] getExecutionsInLayer(String layer) {
        return repository.getExecutionsInHierarchicalLayer(layer);
    }
    
    public String toString() {
        return "Application Statistics[app=" + getApplicationName() + ", inst=" + getInstanceId()
                + "] from " + startTime + ", until " + endTime;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }

    void addToLastInvocations(Tree opTree) {
        if (opTree == null) {
            return;
        }
        
        if (lastInvocationsList.size() >= maxLastInvocations) {
            lastInvocationsList.removeLast();
        }

        lastInvocationsList.addFirst(opTree);
        
        hasStatistics = true;
    }
    
    void addToLastInvocations(List trees) {
        for (Iterator i = trees.iterator(); i.hasNext();) {
            addToLastInvocations((Tree) i.next());
        }
    }

    void mergeTree(Tree opTree) {
        if (opTree == null) {
            return;
        }

        this.tree.merge(opTree);
        hasStatistics = true;
    }
    
    void mergeTree(AggregateOperationTree aggTree) {
        if (aggTree == null) {
            return;
        }

        this.tree.merge(aggTree);
        hasStatistics = true;
    }
    
    void mergeStartAndEndTimes(OperationStatistics stats) {
        mergeStartAndEndTimes(stats.getStartTime(), stats.getEndTime());
    }
    
    void mergeStartAndEndTimes(ApplicationStatistics stats) {
        mergeStartAndEndTimes(stats.getStartTime(), stats.getEndTime());
    }
    
    void mergeStartAndEndTimes(long othersStartTime, long othersEndTime) {
        this.startTime = Math.min(this.startTime, othersStartTime);
        this.endTime = Math.max(this.endTime, othersEndTime);
    }
    
    void mergeLayerAndExecutionTimes(OperationStatistics stats) {
        String[] layers = stats.getLayers();
        for (int i = 0; i < layers.length; i++) {
            String aLayer = layers[i];
            mergeLayerTime(aLayer, stats.getTimeInLayer(aLayer));
            mergeExecutionTimes(aLayer, stats.getExecutions(aLayer));
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
        
    void mergeLayerTime(String layer, long time) {
        repository.mergeHierarchicalLayerTime(layer, time);
        hasStatistics = true;
    }
    
    void mergeExecutionTimes(String layerName, ExecutionTimer[] times) {
        repository.mergeExecutionTimes(layerName, times);
        hasStatistics = true;
    }  
    
    void mergeExecutionTimes(String layerName, AggregateExecutionTime[] times) {
        repository.mergeExecutionTimes(layerName, times);
        hasStatistics = true;
    }  
}
