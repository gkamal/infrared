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
 * Contributor(s):   roopali.agrawal, binil.thomas;
 *
 */
package net.sf.infrared.agent;

import net.sf.infrared.base.model.ExecutionTimer;

/**
 * Facade interface that implements methods to interact with the InfraRED
 * engine. The interface defines methods that can be used to indicate start/end
 * of a method execution and also execution of sql queries etc. Typically these
 * calls are made from code woven into the application code by the aspects. They
 * can also be invoked directly from with the application code, but is not
 * recommended. {@link net.sf.infrared.bootstrap.MonitorFactory}.
 * 
 * @author kamal.govindraj
 * @author binil.thomas
 * @author roopali.agrawal
 */
public interface MonitorFacade {
    /**
     * Call-back method used by monitoring aspects to indicate that an
     * execution has started. <br>
     */        
    public StatisticsCollector recordExecutionBegin(ExecutionTimer timer);

    /**
     * Call-back method for monitoring aspects to indicate that aa
     * execution has stopped. 
     */
    public void recordExecutionEnd(ExecutionTimer timer);
    
    public void recordExecutionEnd(ExecutionTimer timer, StatisticsCollector collector);

    /**
     * 
     */
    public boolean isMonitoringEnabled();

    /**
     * Get the configuration object associated with this monitoring kit
     * 
     * @return
     */
    public MonitorConfig getConfiguration();
    
    public String getApplicationName();
    
    public String getInstanceId();
    
    void destroy();
}
