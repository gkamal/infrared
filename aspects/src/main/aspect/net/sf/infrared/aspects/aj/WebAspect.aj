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

package net.sf.infrared.aspects.aj;

import net.sf.infrared.aspects.aj.InfraREDBaseAspect;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.JspPage;
import javax.servlet.Filter;

/**
* An aspect to instrument all invocations to Servlets
*/

public aspect WebAspect extends InfraREDBaseAspect {
	public pointcut condition() : execution(public * HttpServlet+.*(..))
						|| execution (public * Filter+.*(..));


	public String getApiType() {
	    return "Web";
	}
}
