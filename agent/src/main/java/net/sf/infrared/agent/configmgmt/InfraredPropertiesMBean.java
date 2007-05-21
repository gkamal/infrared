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

/**
 * The MBean interface encapsulating the infrared properties.
 * 
 * @author subin.p
 */
public interface InfraredPropertiesMBean {

    public void setMonitoring(boolean monitoring);

    public boolean getMonitoring();

    public void setCallTraceProfiling(boolean callTraceProfiling);

    public boolean getCallTraceProfiling();

    public void setJdbcMonitoringEnabled(boolean jdbcMonitoringEnabled);

    public boolean getJdbcMonitoringEnabled();

    public void setCollectFetchDataEnabled(boolean collectFetchDataEnabled);

    public boolean getCollectFetchDataEnabled();

    public void setPruneBelowTime(long pruneBelowTime);

    public long getPruneBelowTime();

    // public String getUrlIgnoreList();
    // public void setUrlIgnoreList(String urlIgnoreList);
}
