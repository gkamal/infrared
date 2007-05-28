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
 * Contributor(s):   prashant.nair;
 *
 */

package net.sf.infrared.aspects.aj;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.aspects.api.ApiContext;


/**
 * Base Aspect for AspectJ that is executed for all pointcuts
 */
public abstract aspect InfraREDBaseAspect {
    /**
     * The condition based on which monitoring is performed.
     */
    public abstract pointcut condition();

    /**
     * Gets the type (Session Bean/Entity Bean/JDBC etc.) of API.
     */
    public abstract String getApiType();

    /**
    *  Tracks the time taken by a method call and updates the statistics
    **/
    Object around() : condition(){
      final Class classObj= thisJoinPointStaticPart.getSignature().getDeclaringType();
	  final String methodName = thisJoinPointStaticPart.getSignature().getName();
	  final String apiType = getApiType();
	  Object returnVal;
	  ApiContext ctx = new ApiContext(classObj.getName(), methodName, apiType);

	  MonitorFacade facade = MonitorFactory.getFacade();
	  MonitorConfig cfg = facade.getConfiguration();
	
	  if (cfg.isMonitoringEnabled()) {
	     ExecutionTimer timer = new ExecutionTimer(ctx);
	     MonitorFactory.getFacade().recordExecutionBegin(timer);
	     try {
	         returnVal = proceed();
	     } finally {
	         MonitorFactory.getFacade().recordExecutionEnd(timer);
	     }
	  } else {
	     returnVal = proceed();
	  }
	    
	  return returnVal;

    }    
}
