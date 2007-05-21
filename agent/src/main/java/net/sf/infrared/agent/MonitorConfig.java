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
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent;

/**
 * Interface for acessing / updating configuration parameters
 */
public interface MonitorConfig {
    /**
     * Indicates whether the collection of summary statistics at method level is
     * enabled
     * 
     * @return
     */
    boolean isMonitoringEnabled();

    void enableMonitoring(boolean enable);

    boolean isMonitoringEnabledForCurrentThread();

    void enableMonitoringForCurrentThread(boolean enable);

    boolean isCallTracingEnabled();

    void enableCallTracing(boolean enable);

    // boolean isJDBCMonitoringEnabled();
    // void enableJDBCMonitoring(boolean enable);
    //
    // boolean isJDBCFecthStatisticsCollectionEnabled();
    // void enableJDBCFecthStatisticsCollection(boolean enable);
    //
    // boolean isJDBCResourceLeakDetectionEnabled();
    // void enableJDBCResourceLeakDetectionEnabled(boolean enable);
    //
    // boolean isJDBCTrackResourceAquisitionLocationEnabled();
    // void enableJDBCTrackResourceAquisitionLocationEnabled(boolean enable);

    long getPruneThreshold();

    void setPruneThreshold(long pruneThreshold);

    int getNoOfLastInvocationsToBeTracked();

    void setNoOfLastInvocationsToBeTracked(int noOfLastInvocationsToBeTracked);

    // dev specific parameters
    // boolean isDefensiveChecksEnabled();
    // void setDefensiveChecksEnabled(boolean enable);
    // boolean isDebugLoggingEnabled();

    String getCollectionStrategy();

    // String getMBeanServerProvider();

    // String getUrlIgnoreList();
    // void setUrlIgnoreList(String urlIgnoreList);
    //    
    // String[] getSplitUrlIgnoreList();

    /**
     * Retrieve the value for given parameter name This method is make it easier
     * to add new properties without having to modify all the classes.
     * 
     * @param propertyName
     * @return
     */
    String getProperty(String propertyName, String defaultValue);

    void setProperty(String propertyName, String value);

    int getProperty(String propertyName, int defaultValue);

    void setProperty(String propertyName, int value);

    long getProperty(String propertyName, long defaultValue);

    void setProperty(String propertyName, long value);

    boolean getProperty(String propertyName, boolean defaultValue);

    void setProperty(String propertyName, boolean value);
    
    // void setIsCallFromInfraredPropertiesMBean(boolean value);
}
