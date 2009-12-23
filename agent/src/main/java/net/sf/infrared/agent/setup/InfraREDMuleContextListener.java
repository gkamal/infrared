package net.sf.infrared.agent.setup;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sf.infrared.agent.MonitorConfigImpl;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;
import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.MuleContextNotification;

public class InfraREDMuleContextListener implements MuleContextNotificationListener {
    private static final Logger log = 
        LoggingFactory.getLogger(InfraREDMuleContextListener.class);

    private InfraREDLifeCycleListener lifeCycleListener;
    
	public InfraREDMuleContextListener() {
		lifeCycleListener = new InfraREDLifeCycleListener();
	}

	public void onNotification(ServerNotification notification) {
		String configProvider = MonitorConfigImpl.DEFAULT_CONFIG_LOCATION;
		//TODO determine the app name from config file, if available
        String appName = "muleApp";
        
		log.debug("get notification:" + notification.getAction());
		if(notification.getAction() == MuleContextNotification.CONTEXT_STARTED){
	        log.debug("Initializing infra life cycle listener");
	        getLifeCycleListener().initialized(appName, getInstanceId(), configProvider);
	        log.debug("listener initialized");
		}else if(notification.getAction() == MuleContextNotification.CONTEXT_STOPPED){
			getLifeCycleListener().destroyed(appName);
		}
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
