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
 * Original Author:  roopali.agrawal (Tavant Technologies)
 * Contributor(s):   binil.thomas, kamal.govindraj
 *
 */
package net.sf.infrared.agent;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import net.sf.infrared.agent.util.AgentHelper;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * Acts as the factory for creating MonitorFacade objects. <p/> The aspects that
 * need to advice a method call/execution for collecting monitoring data, gets
 * the MonitorFacade instances from this factory.
 * 
 * @author roopali.agrawal
 * @author binil.thomas
 * @author kamal.govindraj
 */
public class MonitorFactory {
    public static final String DEFAULT_CONFIG_LOCATION = "infrared-agent-root.properties";
    
    private static Logger log;

    private static Map classLoaderToMonitorFacadeMap = new WeakHashMap();

    private static MonitorFacade defaultFacade = null;

    private static final AgentHelper helper = new AgentHelper();

    static {
        init();
    }

    public static MonitorFacade getFacade() {
        ClassLoader loaderForThisApp = Thread.currentThread().getContextClassLoader();
        MonitorFacade facade = getFacadeImplForCurrentApplication(loaderForThisApp);
        if (facade == null) {
            facade = defaultFacade;
        }
        return facade;
    }

    public static void registerFacadeImpl(MonitorFacade facade) {
        registerFacadeImpl(facade, Thread.currentThread().getContextClassLoader());
    }

    public static void registerFacadeImpl(MonitorFacade facade, ClassLoader loader) {
        classLoaderToMonitorFacadeMap.put(loader, facade);

        if (log.isDebugEnabled()) {
            log.debug("Registered " + facade);
        }
    }

    public static void unregisterFacadeImpl(MonitorFacade facade) {
        for (Iterator i = classLoaderToMonitorFacadeMap.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            if (entry.getValue() == facade) {
                classLoaderToMonitorFacadeMap.remove(entry.getKey());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Unregistered " + facade);
        }
    }
    
    public static MonitorFacade unregisterFacadeImpl() {
        MonitorFacade removedFacade = null;
        removedFacade = (MonitorFacade) classLoaderToMonitorFacadeMap.remove(
                Thread.currentThread().getContextClassLoader());
        if (log.isDebugEnabled()) {
            log.debug("UnRegistered " + removedFacade);
        }
        return removedFacade;
    }

    public static void reset() {
        classLoaderToMonitorFacadeMap.clear();
    }
    
    public static MonitorFacade getDefaultFacade() {
        return defaultFacade;
    }

    private static MonitorFacade getFacadeImplForCurrentApplication(ClassLoader loader) {
    	    MonitorFacade facade = (MonitorFacade) classLoaderToMonitorFacadeMap.get(loader);
    	    if (facade == null) {
    	        ClassLoader parent = loader.getParent();
    	        if (parent != null) {
    	            facade = getFacadeImplForCurrentApplication(parent);
    	        }
    	    }
    	    return facade;
    }
    
    private static void init() {
        try {
            log = LoggingFactory.getLogger(MonitorFactory.class);
        } catch (Throwable th) {
            System.out.println("Error calling LoggingFactory.getLogger(MonitorFactory.class)");
            th.printStackTrace();
        }

        setupDefaultMonitorFacade();
        System.out.println(getStartupMessage());
    }

    private static String getStartupMessage() {
        String version = helper.getVersion();
        
        String msg = "\n" +
                     "***************************************************************\n" +
                     "* InfraRED version " + version + "\n" +
                     "*\n";
        if (LoggingFactory.isLoggingConfigured()) {
            if (LoggingFactory.isDefultLoggingUsed()) {
                msg += "* Configured default logging system from " + 
                    LoggingFactory.getLoggingConfiguration() + ". No debug messages would be logged.\n"; 
            } else {
                if (LoggingFactory.isDebugLoggingEnabled()) {
                    msg += "* Configured logging system from " + 
                        LoggingFactory.getLoggingConfiguration() + ".\n"; 
                } else {
                    msg += "* Configured logging system from " + 
                        LoggingFactory.getLoggingConfiguration() + 
                        ". No debug messages would be logged as -D" + 
                        LoggingFactory.DEBUG_KEY + "=true JVM PARAM is not set.\n"; 
                }
            }
        } else {
            msg +=   "* Failed to configure logging system\n";  
        }
        msg +=       "***************************************************************\n";
        return msg;
    }

    private static void setupDefaultMonitorFacade() {
        String applicationName = "default-facade";
        String hostName = helper.getLocalHostName();
        MonitorConfig defaultConfig = new MonitorConfigImpl(DEFAULT_CONFIG_LOCATION);
 
        MonitorFacade facade = new MonitorFacadeImpl(applicationName, hostName, defaultConfig, false);
        defaultFacade = new MultipleEntryGuard(facade);
    }
}
