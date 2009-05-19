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

import net.sf.infrared.web.util.ViewUtil;

import org.apache.struts.action.ActionForm;

public class SqlStatisticsFormBean extends ActionForm {
    
    private String sql;
    private String avgTotalTime;
    private String avgExecuteTime;
    private String avgPrepareTime;
    private String noOfExecutes;
    private String noOfPrepares;
    private String maxExecuteTime;
    private String maxPrepareTime;
    private String minExecuteTime;
    private String minPrepareTime;
    private String lastExecuteTime;
    private String lastPrepareTime;
    private String firstExecuteTime;
    private String firstPrepareTime;
    private String totalExecuteTime;
    private String totalPrepareTime;
    private String tdStyle;
    
    public String getAvgExecuteTime() {
        return ViewUtil.getFormattedTime(avgExecuteTime, 2);
    }
    public void setAvgExecuteTime(String avgExecuteTime) {
        this.avgExecuteTime = avgExecuteTime;
    }
    public String getAvgPrepareTime() {
        return ViewUtil.getFormattedTime(avgPrepareTime, 2);
    }
    public void setAvgPrepareTime(String avgPrepareTime) {
        this.avgPrepareTime = avgPrepareTime;
    }
    public String getAvgTotalTime() {
        return ViewUtil.getFormattedTime(avgTotalTime, 2);
    }
    public void setAvgTotalTime(String avgTotalTime) {
        this.avgTotalTime = avgTotalTime;
    }
    public String getFirstExecuteTime() {
        return firstExecuteTime;
    }
    public void setFirstExecuteTime(String firstExecuteTime) {
        this.firstExecuteTime = firstExecuteTime;
    }
    public String getFirstPrepareTime() {
        return firstPrepareTime;
    }
    public void setFirstPrepareTime(String firstPrepareTime) {
        this.firstPrepareTime = firstPrepareTime;
    }
    public String getLastExecuteTime() {
        return lastExecuteTime;
    }
    public void setLastExecuteTime(String lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }
    public String getLastPrepareTime() {
        return lastPrepareTime;
    }
    public void setLastPrepareTime(String lastPrepareTime) {
        this.lastPrepareTime = lastPrepareTime;
    }
    public String getMaxExecuteTime() {
        return maxExecuteTime;
    }
    public void setMaxExecuteTime(String maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }
    public String getMaxPrepareTime() {
        return maxPrepareTime;
    }
    public void setMaxPrepareTime(String maxPrepareTime) {
        this.maxPrepareTime = maxPrepareTime;
    }
    public String getMinExecuteTime() {
        return minExecuteTime;
    }
    public void setMinExecuteTime(String minExecuteTime) {
        this.minExecuteTime = minExecuteTime;
    }
    public String getMinPrepareTime() {
        return minPrepareTime;
    }
    public void setMinPrepareTime(String minPrepareTime) {
        this.minPrepareTime = minPrepareTime;
    }
    public String getNoOfExecutes() {
        return noOfExecutes;
    }
    public void setNoOfExecutes(String noOfExecutes) {
        this.noOfExecutes = noOfExecutes;
    }
    public String getNoOfPrepares() {
        return noOfPrepares;
    }
    public void setNoOfPrepares(String noOfPrepares) {
        this.noOfPrepares = noOfPrepares;
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public String getTotalExecuteTime() {
        return totalExecuteTime;
    }
    public void setTotalExecuteTime(String totalExecuteTime) {
        this.totalExecuteTime = totalExecuteTime;
    }
    public String getTotalPrepareTime() {
        return totalPrepareTime;
    }
    public void setTotalPrepareTime(String totalPrepareTime) {
        this.totalPrepareTime = totalPrepareTime;
    }    
    public String getTdStyle() {
		if (Double.parseDouble(avgExecuteTime) > 100)
			tdStyle = "hotSpot";
		else
			tdStyle = "";

		return tdStyle;
	}


}
