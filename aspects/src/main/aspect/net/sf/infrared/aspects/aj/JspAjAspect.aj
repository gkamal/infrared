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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */

package net.sf.infrared.aspects.aj;


import javax.servlet.jsp.JspPage;
import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.aspects.jsp.JspContext;
import net.sf.infrared.base.model.ExecutionTimer;

/**
* An aspect to instrument all invocations to JSP pages 
*/

public aspect JspAjAspect {
	public pointcut condition() : execution(public void javax.servlet.jsp.HttpJspPage+
											._jspService(javax.servlet.http.HttpServletRequest, 
														javax.servlet.http.HttpServletResponse));

    Object around() : condition(){
      final Class classObj= thisJoinPointStaticPart.getSignature().getDeclaringType();

	  Object returnVal;
	  JspContext ctx = new JspContext(classObj);

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
