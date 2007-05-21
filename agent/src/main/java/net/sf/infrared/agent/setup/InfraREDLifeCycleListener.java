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

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorConfigImpl;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFacadeImpl;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.MultipleEntryGuard;
import net.sf.infrared.agent.configmgmt.InfraredProperties;
import net.sf.infrared.base.configmgmt.AbstractMBeanServerFactory;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * Implements the lifecycle methods for InfraRED.
 */
public class InfraREDLifeCycleListener {

    private static final Logger log = LoggingFactory.getLogger(InfraREDLifeCycleListener.class);

    private static final String KEY_MBEAN_SERVER_PROVIDER = "mbean-server-provider";
    
    private MBeanServer mbeanServer = null;

    private ObjectName propertiesObjectName;

    private MonitorFacade facade = null;

    /**
     * The default constructor
     */
    public InfraREDLifeCycleListener() {
    }

    /**
     * Initialized infraRED agent for the specified application name. It is
     * important that the threads context class loader is setup correctly when
     * this call is made. All subsequently calls to infraRED orginating from
     * this application should have the class loader or a child class loader as
     * the threads context class loader. This is usually true if the methods are
     * being called from a ServletContextListener or an ApplicationLifeCycleListener
     * 
     * @param applicationName
     */
    public void initialized(String applicationName, String instanceId, String configProvider) {
        MonitorConfig config = new MonitorConfigImpl(configProvider);

        facade = new MonitorFacadeImpl(applicationName, instanceId, config, true);
        facade = new MultipleEntryGuard(facade);

        MonitorFactory.registerFacadeImpl(facade);
        if (log.isDebugEnabled()) {
            log.debug("Initializing application : " + applicationName + " InstanceId : "
                    + instanceId);
        }

        // Get the MBeanServer instance and register the MBeans.
        setMBeanServer(config, applicationName, instanceId);
        registerMBeans(config);
    }

    /**
     * Reset the infrared agent setting for the specified applciation
     * 
     * @param applicationName
     */
    public void destroyed(String applicationName) {
        // Unregister the MBeans before unregistering the FacadeImpl
        unregisterMBeans();

        // MonitorFactory.unregisterFacadeImpl(facade);
        // unregisters the Facade belonging to this threads context classloader
        MonitorFacade facade = MonitorFactory.unregisterFacadeImpl();
        if (facade != null) {
            facade.destroy();
        }
        if (log.isDebugEnabled()) {
            log.debug("Destroying application " + applicationName);
        }
    }

    /**
     * Method to register the MBeans with the MBeanServer if the MBeanServer is
     * not null
     */
    public void registerMBeans(MonitorConfig config) {
        if (mbeanServer != null) {
            InfraredProperties infraredProperties = new InfraredProperties(config);
            try {
                String objectName = "InfraredProperties:name=Infrared Properties,"
                        + "type=InfraredPropertiesMBean";
                propertiesObjectName = new ObjectName(objectName);
                mbeanServer.registerMBean(infraredProperties, propertiesObjectName);
                log.debug("The MBean has been successfully registered !");
            } catch (MalformedObjectNameException e) {
                log.error("The MBean Object name is Malformed", e);
            } catch (InstanceAlreadyExistsException e) {
                log.error("The MBean instance already exists", e);
            } catch (MBeanRegistrationException e) {
                log.error("The MBean Registration failed", e);
            } catch (NotCompliantMBeanException e) {
                log.error("The MBean is not Compliant &  Registration failed", e);
            }
        }
    }

    public void unregisterMBeans() {
        if (mbeanServer == null) {
            return;
        }

        try {
            mbeanServer.unregisterMBean(propertiesObjectName);
        } catch (MBeanRegistrationException e) {
            log.error("Unable to unregister the MBean", e);
        } catch (InstanceNotFoundException e) {
            log.error("The MBean instance is not registered in the MBeanServer", e);
        }
    }

    public MonitorFacade getFacade() {
        return facade;
    }

    private void setMBeanServer(MonitorConfig config, String appName, String instanceId) {
        String mbeanServerClass = config.getProperty(KEY_MBEAN_SERVER_PROVIDER, (String) null);
        if (mbeanServerClass == null) {
            log.warn("No MBean Server Class set for application " + appName 
                    + ", instance " + instanceId);
            return;
        }
        try {
            AbstractMBeanServerFactory mbeanServerProvider = 
                (AbstractMBeanServerFactory) Class.forName(mbeanServerClass).newInstance();
            mbeanServer = mbeanServerProvider.getMBeanServer();
        } catch (Exception e) {
            log.error("Unable to load MBeanServer provider", e);
        }
    }
}
