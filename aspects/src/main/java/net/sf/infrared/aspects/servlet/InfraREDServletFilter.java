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
package net.sf.infrared.aspects.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 *
 * @author binil.thomas
 */
public class InfraREDServletFilter implements Filter {    
    private static final Logger log = LoggingFactory.getLogger(InfraREDServletFilter.class);
    
    public InfraREDServletFilter() {
        log.debug("Created InfraREDServletFilter");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        log.debug("Invoked InfraREDServletFilter");
        if (request instanceof HttpServletRequest) {
            
            HttpServletRequest httpReq = (HttpServletRequest) request;
            String uri = httpReq.getRequestURI();
            if (log.isDebugEnabled()) {
                log.debug("This is an HTTP request InfraREDServletFilter, so capturing the URI " + uri);
            }
            ExecutionContext ctx = new ServletContext(uri);
            
            ExecutionTimer timer = new ExecutionTimer(ctx);
            
            MonitorFacade facade = MonitorFactory.getFacade();
            
            facade.recordExecutionBegin(timer);
            chain.doFilter(request, response);
            facade.recordExecutionEnd(timer);
        } else {
            log.debug("This is not an HTTP request InfraREDServletFilter, so ignoring");
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Initialized InfraREDServletFilter");
    }

    public void destroy() {
        log.debug("Destroyed InfraREDServletFilter");
    }        
}
