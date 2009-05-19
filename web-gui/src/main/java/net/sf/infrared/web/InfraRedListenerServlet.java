/*
 *
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
 * Contributor(s):   prashant.nair;
 *
 */
package net.sf.infrared.web;

import java.io.IOException;
import java.net.BindException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.Collector;
import net.sf.infrared.collector.CollectorConfig;
import net.sf.infrared.collector.impl.CollectorImpl;
import net.sf.infrared.web.util.DataFetchUtil;
import net.sf.infrared.web.util.WebConfig;

import org.apache.log4j.Logger;

/**
 *
 * @kamal.govindraj
 * @author prashant.nair
 */
public class InfraRedListenerServlet extends HttpServlet {
    public static final String PORT_KEY = "port";

    public static final int DEFAULT_PORT = 9001;

    private static final Logger logger = 
                            LoggingFactory.getLogger(InfraRedListenerServlet.class.getName());

    private int port = DEFAULT_PORT;
    
    private Collector collector;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initCollector(config);
    }

    /**
     * Shuts down the listener. Called by the container when the application is 
     * stopped/undeployed
     */
    public void destroy() {
        if (collector != null) {
            collector.shutdown();
        }
    }

    /**
     * Produces an HTML page which lists the agents which are currently connected to the
     * central collector. This can be used as a deployment aid.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) 
                                throws ServletException,IOException {
        //@TODO provide an implementation that lists the agent that are 
        // connected to it.
    }

    void initCollector(ServletConfig config) throws ServletException {
        // The value of the port is read from the servlet config 
        // which is set in web.xml
        String portVal = config.getInitParameter(PORT_KEY);
        try {
            if (portVal != null) {
                port = Integer.parseInt(portVal);
            }
        }
        catch (NumberFormatException numex) {
            String msg = "The value of 'port' init parameter, '" + portVal
                    + "', is not valid one. " + "Legal values are non-zero positive integers";
            logger.error(msg, numex);
            throw new ServletException(msg, numex); // stop the deployment
        }
        if (port <= 0) {
            String msg = "The value of 'port' init parameter, '" + portVal
                    + "', is not valid one. " + "Legal values are non-zero positive integers";
            logger.error(msg);
            throw new ServletException(msg);
        }
        try {
            startListening();
            if (logger.isDebugEnabled()) {
                logger.debug("AgentListener thread spawned to listen on port " + port);
            }
        }
        catch (BindException bindex) {
            String msg = "Error while attempting to start listening; port specified is " + port
                    + ". " + "Check if this port is in use by another application";
            logger.error(msg, bindex);
            throw new ServletException(msg, bindex);
        }
        catch (IOException e) {
            String msg = "Error while attempting to start listening";
            logger.error(msg, e);
            throw new ServletException(msg, e);
        }

    }

    void startListening() throws IOException {
        Collector collector = new CollectorImpl();
        collector.start(new CollectorConfig(){
            public int getAgentListenPort(){
                return port;
            }            
            public long getPersistInterval(){
                return WebConfig.getPersistInterval();
            }
        });
        DataFetchUtil.setCollector(collector);
    }

    int getPort() {
        return this.port;
    }

}
