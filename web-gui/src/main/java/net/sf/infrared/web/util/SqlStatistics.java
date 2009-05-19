/*
 *
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
package net.sf.infrared.web.util;

import net.sf.infrared.base.model.AggregateExecutionTime;

public class SqlStatistics {

    private String sql = null;
    private long noOfExecutes;
    private long noOfPrepares;
    private long maxExecuteTime;
    private long maxPrepareTime;
    private long minExecuteTime = Long.MAX_VALUE;
    private long minPrepareTime = Long.MAX_VALUE;
    private long lastExecuteTime;
    private long lastPrepareTime;
    private long firstExecuteTime;
    private long firstPrepareTime;
    private long totalExecuteTime;
    private long totalPrepareTime;
    private long timeOfFirstPrepare = Long.MAX_VALUE;
    private long timeOfLastPrepare;
    private long timeOfFirstExecute = Long.MAX_VALUE;
    private long timeOfLastExecute;
    
    public double getAvgExecuteTime() {
        if (this.noOfExecutes == 0) {
            return 0;
        }
        else {
            return ((double) this.totalExecuteTime / this.noOfExecutes);
        }
    }    
    public double getAvgTotalTime() {
        return  getAvgExecuteTime() + getAvgPrepareTime();
    }    
    public void setTotalExecuteTime(long totalExecuteTime) {
        this.totalExecuteTime = totalExecuteTime;
    }
    public double getAvgPrepareTime() {
        if (this.noOfPrepares == 0) {
            return 0;
        }
        else {
            return ((double) this.totalPrepareTime / this.noOfPrepares);
        }
    }
    public void setTotalPrepareTime(long totalPrepareTime) {
        this.totalPrepareTime = totalPrepareTime;
    }
    public long getFirstExecuteTime() {
        return firstExecuteTime;
    }
    public void setFirstExecuteTime(long firstExecuteTime) {
        this.firstExecuteTime = firstExecuteTime;
    }
    public long getFirstPrepareTime() {
        return firstPrepareTime;
    }
    public void setFirstPrepareTime(long firstPrepareTime) {
        this.firstPrepareTime = firstPrepareTime;
    }
    public long getLastExecuteTime() {
        return lastExecuteTime;
    }
    public void setLastExecuteTime(long lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }
    public long getLastPrepareTime() {
        return lastPrepareTime;
    }
    public void setLastPrepareTime(long lastPrepareTime) {
        this.lastPrepareTime = lastPrepareTime;
    }
    public long getMaxExecuteTime() {
        return maxExecuteTime;
    }
    public void setMaxExecuteTime(long maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }
    public long getMaxPrepareTime() {
        return maxPrepareTime;
    }
    public void setMaxPrepareTime(long maxPrepareTime) {
        this.maxPrepareTime = maxPrepareTime;
    }
    public long getMinExecuteTime() {
    	if(this.noOfExecutes == 0){
    		return 0;
    	}
    	else{
    		return minExecuteTime;
    	}
        
    }
    public void setMinExecuteTime(long minExecuteTime) {
        this.minExecuteTime = minExecuteTime;
    }
    public long getMinPrepareTime() {
    	if(this.noOfPrepares == 0){
    		return 0;
    	}
    	else{
    		return minPrepareTime;
    	}
        
    }
    public void setMinPrepareTime(long minPrepareTime) {
        this.minPrepareTime = minPrepareTime;
    }
    public long getNoOfExecutes() {
        return noOfExecutes;
    }
    public void setNoOfExecutes(long noOfExecutes) {
        this.noOfExecutes = noOfExecutes;
    }
    public long getNoOfPrepares() {
        return noOfPrepares;
    }
    public void setNoOfPrepares(long noOfPrepares) {
        this.noOfPrepares = noOfPrepares;
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    
    public void mergePrepareTime(AggregateExecutionTime aggrExec){
    	if(sql == null){
    		setSql(aggrExec.getContext().getName());
    	}
    	if(aggrExec.getTimeOfFirstExecution() < timeOfFirstPrepare ){
    		timeOfFirstPrepare = aggrExec.getTimeOfFirstExecution();
    		firstPrepareTime = aggrExec.getInclusiveFirstExecutionTime();
    	}
    	if(aggrExec.getTimeOfLastExecution() > timeOfLastPrepare){
    		timeOfLastPrepare = aggrExec.getTimeOfLastExecution();
    		lastPrepareTime = aggrExec.getInclusiveLastExecutionTime();
    	}
    	if(aggrExec.getMaxInclusiveTime() > maxPrepareTime){
    		maxPrepareTime = aggrExec.getMaxInclusiveTime();
    	}
    	if(aggrExec.getMinInclusiveTime() < minPrepareTime){
    		minPrepareTime = aggrExec.getMinInclusiveTime();
    	}
    	noOfPrepares+=aggrExec.getExecutionCount();
    	totalPrepareTime+=aggrExec.getTotalInclusiveTime();
    }

    public void mergeExecuteTime(AggregateExecutionTime aggrExec){
    	if(sql == null){
    		setSql(aggrExec.getContext().getName());
    	}
    	if(aggrExec.getTimeOfFirstExecution() < timeOfFirstExecute ){
    		timeOfFirstExecute = aggrExec.getTimeOfFirstExecution();
    		firstExecuteTime = aggrExec.getInclusiveFirstExecutionTime();
    	}
    	if(aggrExec.getTimeOfLastExecution() > timeOfLastExecute){
    		timeOfLastExecute = aggrExec.getTimeOfLastExecution();
    		lastExecuteTime = aggrExec.getInclusiveLastExecutionTime();
    	}
    	if(aggrExec.getMaxInclusiveTime() > maxExecuteTime){
    		maxExecuteTime = aggrExec.getMaxInclusiveTime();
    	}
    	if(aggrExec.getMinInclusiveTime() < minExecuteTime){
    		minExecuteTime = aggrExec.getMinInclusiveTime();
    	}
    	noOfExecutes+=aggrExec.getExecutionCount();
    	totalExecuteTime+=aggrExec.getTotalInclusiveTime();
    	
    }
}
