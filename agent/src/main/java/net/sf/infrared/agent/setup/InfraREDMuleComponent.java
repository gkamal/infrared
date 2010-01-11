package net.sf.infrared.agent.setup;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sf.infrared.agent.MonitorConfigImpl;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

public class InfraREDMuleComponent  {
    private static final Logger log = 
        LoggingFactory.getLogger(InfraREDMuleComponent.class);

    private static InfraREDLifeCycleListener lifeCycleListener;
    
	public InfraREDMuleComponent() {
		lifeCycleListener = new InfraREDLifeCycleListener();
	}

	static
	{
		lifeCycleListener = new InfraREDLifeCycleListener();
		initialize();
	}
	
	public static void initialize() {
		String configProvider = MonitorConfigImpl.DEFAULT_CONFIG_LOCATION;
		//TODO determine the app name from config file, if available
        String appName = "muleApp";
        
          lifeCycleListener.initialized(appName, getInstanceId(), configProvider);
	        log.debug("listener initialized");
	}

    public static String getInstanceId() {
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
