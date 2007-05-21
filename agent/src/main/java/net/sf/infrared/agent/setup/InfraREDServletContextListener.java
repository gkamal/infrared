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
package net.sf.infrared.agent.setup;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.infrared.agent.MonitorConfigImpl;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * ServletContextListener implementation that setups up infraRED.
 */
public class InfraREDServletContextListener implements ServletContextListener {
    public static final String KEY_CONFIGURATIONPROVIDER = "net.sf.infrared.configurationprovider";

    private static final Logger log = 
        LoggingFactory.getLogger(InfraREDServletContextListener.class);

    private InfraREDLifeCycleListener lifeCycleListener; 
    
    public InfraREDServletContextListener() {
        lifeCycleListener = new InfraREDLifeCycleListener();
    }

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        initialized(context);
    }

    public void contextDestroyed(ServletContextEvent event) {
        destroyed(event.getServletContext());
    }
    
    public void initialized(ServletContext context) {
        String configProvider = 
            (String) context.getAttribute(KEY_CONFIGURATIONPROVIDER);
        if (configProvider == null) {
            configProvider = MonitorConfigImpl.DEFAULT_CONFIG_LOCATION;
        }

        String appName = context.getServletContextName();
        if (appName == null) {
            appName = "unknown";
            log.info("Application name is not set in the war");
        }
        getLifeCycleListener().initialized(appName, getInstanceId(), configProvider);
    }
    
    public void destroyed(ServletContext context) {
        getLifeCycleListener().destroyed(context.getServletContextName());
    }

    public String getInstanceId() {
        String instanceId = "unknown";
        try {
            instanceId = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error(e);
        }
        return instanceId;
    }
    
    InfraREDLifeCycleListener getLifeCycleListener() {
        return this.lifeCycleListener;
    }
}
