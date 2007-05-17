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

import java.util.List;
import java.util.Map;

import net.sf.infrared.base.util.Tree;

/**
 * Represents the statistics collected for a given operation. An operation represents a single 
 * request that executes in one thread.
 * 
 * @author binil.thomas
 */
public class OperationStatistics extends AbstractStatistics {
    // tree of executions for this operation. Each value of a TreeNode in the tree
    // is an instance of a ExecutiomTimer object (captured only if call tree statistics
    // is enabled)
    private Tree operationTree;

    // map of heirarchical layer name -> list of ExecutionTimer objects
    // this map identifies the various executions that happened in each layer heirarchy
    private Map executions;

    // map of heirarchical layer name -> LayerTime objects
    // this map identifies the time taken in various layers
    private Map layers;
    
    // system time when the operation started
    private long startTime = -1;
    
    // system time when the operation ended
    private long endTime = -1;
    
    private int numOfExecsTracked = 0;
    
    private int numOfExecsIgnored = 0;
    
    public OperationStatistics(String application, String instance) {
        super(application, instance);
    }

    public Tree getOperationTree() {
        return operationTree;
    }

    public void setOperationTree(Tree tree) {
        if (operationTree != null) {
            throw new IllegalStateException("operation tree is already set; can't set it again");
        }
        this.operationTree = tree;
    }
    
    public void setLayerTimes(Map layerTimes) {
//        if (this.layers != null) {
//            throw new IllegalStateException("layer times are already set; can't set it again");
//        }
//        
//        if (this.executions != null) {
//            Set layerNamesAsPerLayersMap = layerTimes.keySet();
//            Set layerNamesAsPerExecutionsMap = this.executions.keySet();
//            
//            if (! layerNamesAsPerLayersMap.equals( layerNamesAsPerExecutionsMap )) {
//                throw new IllegalArgumentException("execution timings set earlier has a" +
//                        " different set of layers[" + layerNamesAsPerExecutionsMap + "]" +
//                        " which does not match the layers of this layer timings " +
//                        "[" + layerNamesAsPerLayersMap + "]");
//            }
//        }
        this.layers = layerTimes;
    }
    
    public void setExecutionTimes(Map executionTimes) {
//        if (this.executions != null) {
//            throw new IllegalStateException("execution times are already set; can't set again");
//        }
//        
//        if (this.layers != null) {
//            Set layerNamesAsPerLayersMap = this.layers.keySet();
//            Set layerNamesAsPerExecutionsMap = executionTimes.keySet();
//            
//            if (! layerNamesAsPerLayersMap.equals( layerNamesAsPerExecutionsMap )) {
//                throw new IllegalArgumentException("layer timings set earlier has a different" +
//                        " set of layers[" + layerNamesAsPerLayersMap + "] which does not match" +
//                        " the layers of this execution timings " +
//                        "[" + layerNamesAsPerExecutionsMap + "]");
//            }
//        }
        this.executions = executionTimes;
    }
    
    public void setNumOfExecutions(int tracked, int ignored) {
        numOfExecsTracked = tracked;
        numOfExecsIgnored = ignored;
    }

    public long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(long time) {
        if (this.endTime != -1) {
            throw new IllegalStateException("end time is already set; can't set again");
        }
        this.endTime = time;
    }

    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long time) {
        if (this.startTime != -1) {
            throw new IllegalStateException("start time is already set; can't set again");
        }
        this.startTime = time;
    }
    
    public String[] getLayers() {
        if (layers == null) {
            return new String[0];
        }
        return  (String[]) layers.keySet().toArray(new String[0]);
    }
    
    public long getTimeInLayer(String layerName) {
        LayerTime lt = (LayerTime) layers.get(layerName);
        if (lt == null) {
            return -1;
        } else {
            return lt.getTime();
        }
    }
    
    public ExecutionTimer[] getExecutions(String layerName) {
        List l =  (List) executions.get(layerName);
        if (l == null) {
            return new ExecutionTimer[0];
        }
        return (ExecutionTimer[]) l.toArray(new ExecutionTimer[0]);
    }
    
    public long getTimeTaken(){
    	 if (startTime == -1 || endTime == -1) {
             throw new IllegalStateException("Start/End time not still set. Cannot determine time taken"+
            		 "Start time = "+startTime+" ,End time = "+endTime);
         }
    	return endTime - startTime;
    }

    public String toString() {
        return "OperationStatistics[" + getApplicationName() + " on " +getInstanceId()
                + "] started at " + getStartTime() + ", ended at " + getEndTime() 
                + "; captured " + numOfExecsTracked + " executions; ignored " + numOfExecsIgnored
                + "ignored";
    }
}
