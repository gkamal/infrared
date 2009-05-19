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
 *
 * Changes:
 * --------
 *
 */

package net.sf.infrared.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.web.util.PerformanceDataSnapshot;

import org.apache.log4j.Logger;

/**
 * 
 * @author prashant.nair
 * Filter to check for session expiration in infrared.  If the session expires
 * the request gets directed to the performance summary page where the 
 * performance data snapshot is reset in the session.
 */

public class InfraREDSessionFilter implements Filter {
    private static Logger logger = 
                       LoggingFactory.getLogger("net.sf.infrared.web.filter.InfraREDSessionFilter");

    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String value = filterConfig.getInitParameter("excludeUrl");
        String url = ((HttpServletRequest) req).getRequestURI();

        HttpSession session = ((HttpServletRequest) req).getSession();
        PerformanceDataSnapshot perfData = (PerformanceDataSnapshot) session
                .getAttribute("perfData");

        if ((url.indexOf(value) > -1) || (perfData != null)) {
            chain.doFilter(req, res);
        }
        else {
            logger.error(" Creating a new user session. Forwarding to performance summary page");
            ((HttpServletResponse) (res)).sendRedirect("perfData_summaryAction.do");
        }
    }

    public void destroy() {
        this.filterConfig = null;
    }

}
