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
 * Original Author:  Rajnish Prasad (Tavant Technologies)
 * Contributor(s):   -;
 *
 *
 * Changes:
 * --------
 *
 */

package net.sf.infrared.aspects.aspectwerkz;

import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.base.model.ExecutionTimer;

/**
 * Base Aspect for AspectWerkz that is executed for all pointcuts
 */
public class InfraRedBaseAspect
{
    private String apiType;

    public InfraRedBaseAspect(AspectContext aspectContext)
    {
        apiType = aspectContext.getParameter("layer");
    }

    public Object collectMetrics(StaticJoinPoint jp) throws Throwable
    {
        Object returnVal = null;
  	  MonitorFacade facade = MonitorFactory.getFacade();
	  MonitorConfig cfg = facade.getConfiguration();

        if (cfg.isMonitoringEnabled())
        {
            final Class classObj= jp.getSignature().getDeclaringType();
        	 final String methodName = jp.getSignature().getName();

        	 ApiContext ctx = new ApiContext(classObj.getName(), methodName, apiType);

      	  
	   	     ExecutionTimer timer = new ExecutionTimer(ctx);
		     MonitorFactory.getFacade().recordExecutionBegin(timer);

            try
            {
                returnVal = jp.proceed();
            }
            finally
            {
    	         MonitorFactory.getFacade().recordExecutionEnd(timer);
            }
        }
        else
        {
            returnVal = jp.proceed();
        }
        return returnVal;
    }

}
