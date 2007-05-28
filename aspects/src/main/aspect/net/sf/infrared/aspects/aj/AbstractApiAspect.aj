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

package net.sf.infrared.aspects.aj;

import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.StatisticsCollector;

/**
 * An abstract aspect implementation which records the execution of a simple
 * API. Clients can provide concrete implementations which specifies the
 * layer information and the pointcut.
 *
 * @author binil.thomas
 */
public abstract aspect AbstractApiAspect {
    
    public abstract pointcut apiExecution();

    public abstract String getLayer();
    
    Object around(): apiExecution() {
        MonitorFacade facade = MonitorFactory.getFacade();
    	if(! isMonitoringEnabled(facade) ) {
            return proceed();            
    	}
        
        final Class classObj= thisJoinPointStaticPart.getSignature().getDeclaringType();
	    final String methodName = thisJoinPointStaticPart.getSignature().getName();
        ApiContext ctx = new ApiContext(classObj.getName(), methodName, getLayer());
        ExecutionTimer timer = new ExecutionTimer(ctx);

        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {
            return proceed();            
        } finally {
            facade.recordExecutionEnd(timer, collector);            
        }  
    }
    
    private boolean isMonitoringEnabled(MonitorFacade facade){        
        MonitorConfig cfg = facade.getConfiguration();
        return cfg.isMonitoringEnabled();
    }
}