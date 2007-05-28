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
package net.sf.infrared.aspects.api;

import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.aspects.AbstractBaseAspect;

import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * 
 * @author binil.thomas
 * @author prashant.nair
 */
public class ApiAspect extends AbstractBaseAspect {
    private String layerName;

    public ApiAspect(AspectContext aspectContext) {
        layerName = aspectContext.getParameter("layer");
    }

    public Object collectMetrics(StaticJoinPoint sjp) throws Throwable {  
    	MonitorFacade facade = MonitorFactory.getFacade();
    	if(! isMonitoringEnabled(facade) ) {
            return sjp.proceed();
    	} 
    	
        final Class classObj = sjp.getSignature().getDeclaringType();
        final String methodName = sjp.getSignature().getName();
        ApiContext ctx = new ApiContext(classObj.getName(), methodName, layerName);
        return recordExecution(ctx, sjp, facade);
    }
}
