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
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent.configmgmt;

import net.sf.infrared.agent.MonitorConfig;

/**
 * This class provides the implementation for the InfraredPropertiesMBean. The
 * changes made to the properties (via a JMX console) are made to reflect on the
 * MonitorConfig instance.
 * 
 * @author subin.p
 */

// @TODO Need to implement the configuration handling
public class InfraredProperties implements InfraredPropertiesMBean {

    private static final String KEY_JDBC_MONITORING_ENABLED = "jdbc-monitoring-enable";

    private static final String KEY_JDBC_FETCH_STATISTICS_ENABLE = "jdbc-fetch-statistics";
    
    private MonitorConfig config;

    public InfraredProperties(MonitorConfig config) {
        this.config = config;
    }

    public void setMonitoring(boolean monitoring) {
        config.enableMonitoring(monitoring);
    }

    public boolean getMonitoring() {
    	// config.setIsCallFromInfraredPropertiesMBean(true);
    	return config.isMonitoringEnabled();
    }

    public void setCallTraceProfiling(boolean callTraceProfiling) {
        config.enableCallTracing(callTraceProfiling);
    }

    public boolean getCallTraceProfiling() {
    	// config.setIsCallFromInfraredPropertiesMBean(true);
    	return config.isCallTracingEnabled();
    }

    public void setJdbcMonitoringEnabled(boolean jdbcMonitoringEnabled) {
        config.setProperty(KEY_JDBC_MONITORING_ENABLED, jdbcMonitoringEnabled);
    }

    public boolean getJdbcMonitoringEnabled() {
    	// config.setIsCallFromInfraredPropertiesMBean(true);
    	return config.getProperty(KEY_JDBC_MONITORING_ENABLED, true);
    }

    public void setCollectFetchDataEnabled(boolean collectFetchDataEnabled) {
        config.setProperty(KEY_JDBC_FETCH_STATISTICS_ENABLE, collectFetchDataEnabled);
    }

    public boolean getCollectFetchDataEnabled() {
    	// config.setIsCallFromInfraredPropertiesMBean(true);
    	return config.getProperty(KEY_JDBC_FETCH_STATISTICS_ENABLE, true);
    }

    public void setPruneBelowTime(long pruneBelowTime) {
        config.setPruneThreshold(pruneBelowTime);
    }

    public long getPruneBelowTime() {
    	// config.setIsCallFromInfraredPropertiesMBean(true);
    	return config.getPruneThreshold();
    }

    // public String getUrlIgnoreList() {
    // // return config.getUrlIgnoreList();
    // return "";
    // }
    //
    // public void setUrlIgnoreList(String urlIgnoreList) {
    // // config.setUrlIgnoreList(urlIgnoreList);
    // }
}
