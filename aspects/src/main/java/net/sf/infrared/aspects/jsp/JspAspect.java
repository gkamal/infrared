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
package net.sf.infrared.aspects.jsp;

import net.sf.infrared.base.util.LoggingFactory;
import org.apache.log4j.Logger;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

import net.sf.infrared.aspects.AbstractBaseAspect;

public class JspAspect extends AbstractBaseAspect {
    private static final Logger log = LoggingFactory.getLogger(JspAspect.class);
    
    public Object aroundJspExecution(StaticJoinPoint sjp) throws Throwable {        
    	
    	if(!isMonitoringEnabled()) {
    		return sjp.proceed();
    	}     	    	
    	// log.debug("Recording execution of a jsp");        
        JspContext ctx = new JspContext(sjp.getSignature().getDeclaringType());
        // log.debug("Created JSP Context " + ctx + " from " + sjp.getSignature().getDeclaringType());
        Object result = recordExecution(ctx, sjp);
        // log.debug("Finished recording JSP Execution " + ctx);
        return result;
    }
}
