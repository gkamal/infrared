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
package net.sf.infrared.web.formbean;

import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.web.util.ViewUtil;

import org.apache.struts.action.ActionForm;

public class AggrExecTimeFormBean extends ActionForm {

    private ExecutionContext context;
    
    private String executionCount;

    private String totalInclusiveTime;

    private String maxInclusiveTime;

    private String minInclusiveTime;

    private String totalExclusiveTime;

    private String maxExclusiveTime;

    private String minExclusiveTime;

    private String timeOfFirstExecution;

    private String timeOfLastExecution;

    private String inclusiveFirstExecutionTime;

    private String inclusiveLastExecutionTime;

    private String exclusiveFirstExecutionTime;

    private String exclusiveLastExecutionTime;

    private String averageExclusiveTime;
    
    private String averageInclusiveTime;
    
    private String adjAverageInclusiveTime;
    
    private String adjAverageExclusiveTime;
    
    private String layerName;
    
    public String getAdjAverageExclusiveTime() {
        return ViewUtil.getFormattedTime(adjAverageExclusiveTime, 2);
    }

    public void setAdjAverageExclusiveTime(String adjAverageExclusiveTime) {
        this.adjAverageExclusiveTime = adjAverageExclusiveTime;
    }

    public String getAdjAverageInclusiveTime() {
        return ViewUtil.getFormattedTime(adjAverageInclusiveTime, 2);
    }

    public void setAdjAverageInclusiveTime(String adjAverageInclusiveTime) {
        this.adjAverageInclusiveTime = adjAverageInclusiveTime;
    }

    public String getAverageExclusiveTime() {
        return ViewUtil.getFormattedTime(averageExclusiveTime, 2);
    }

    public void setAverageExclusiveTime(String averageExclusiveTime) {
        this.averageExclusiveTime = averageExclusiveTime;
    }

    public String getAverageInclusiveTime() {
        return ViewUtil.getFormattedTime(averageInclusiveTime, 2);
    }

    public void setAverageInclusiveTime(String averageInclusiveTime) {
        this.averageInclusiveTime = averageInclusiveTime;
    }

    public ExecutionContext getContext() {
        return context;
    }
    
    public void setContext(ExecutionContext context) {
        this.context = context;
    }

    public String getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(String count) {
        this.executionCount = count;
    }

    public String getExclusiveFirstExecutionTime() {
        return exclusiveFirstExecutionTime;
    }

    public void setExclusiveFirstExecutionTime(String exclusiveFirstExecutionTime) {
        this.exclusiveFirstExecutionTime = exclusiveFirstExecutionTime;
    }

    public String getExclusiveLastExecutionTime() {
        return exclusiveLastExecutionTime;
    }

    public void setExclusiveLastExecutionTime(String exclusiveLastExecutionTime) {
        this.exclusiveLastExecutionTime = exclusiveLastExecutionTime;
    }

    public String getInclusiveFirstExecutionTime() {
        return inclusiveFirstExecutionTime;
    }

    public void setInclusiveFirstExecutionTime(String inclusiveFirstExecutionTime) {
        this.inclusiveFirstExecutionTime = inclusiveFirstExecutionTime;
    }

    public String getInclusiveLastExecutionTime() {
        return inclusiveLastExecutionTime;
    }

    public void setInclusiveLastExecutionTime(String inclusiveLastExecutionTime) {
        this.inclusiveLastExecutionTime = inclusiveLastExecutionTime;
    }

    public String getMaxExclusiveTime() {
        return maxExclusiveTime;
    }

    public void setMaxExclusiveTime(String maxExclusiveTime) {
        this.maxExclusiveTime = maxExclusiveTime;
    }

    public String getMaxInclusiveTime() {
        return maxInclusiveTime;
    }

    public void setMaxInclusiveTime(String maxInclusiveTime) {
        this.maxInclusiveTime = maxInclusiveTime;
    }

    public String getMinExclusiveTime() {
        return minExclusiveTime;
    }

    public void setMinExclusiveTime(String minExclusiveTime) {
        this.minExclusiveTime = minExclusiveTime;
    }

    public String getMinInclusiveTime() {
        return minInclusiveTime;
    }

    public void setMinInclusiveTime(String minInclusiveTime) {
        this.minInclusiveTime = minInclusiveTime;
    }

    public String getTimeOfFirstExecution() {
        return timeOfFirstExecution;
    }

    public void setTimeOfFirstExecution(String timeOfFirstExecution) {
        this.timeOfFirstExecution = timeOfFirstExecution;
    }

    public String getTimeOfLastExecution() {
        return timeOfLastExecution;
    }

    public void setTimeOfLastExecution(String timeOfLastExecution) {
        this.timeOfLastExecution = timeOfLastExecution;
    }

    public String getTotalExclusiveTime() {
        return totalExclusiveTime;
    }

    public void setTotalExclusiveTime(String totalExclusiveTime) {
        this.totalExclusiveTime = totalExclusiveTime;
    }

    public String getTotalInclusiveTime() {
        return totalInclusiveTime;
    }

    public void setTotalInclusiveTime(String totalInclusiveTime) {
        this.totalInclusiveTime = totalInclusiveTime;
    }

    public String getTruncatedName(){
        return context.getName();
    }
    
    public String getApiName(){
        return context.getName();
    }

    public String getCtxName() {
        return context.getClass().getName();
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
    
    
}
