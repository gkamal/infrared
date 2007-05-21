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
package net.sf.infrared.agent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * 
 * @author binil.thomas
 */
public class MonitorConfigImpl implements MonitorConfig {
    public static final String DEFAULT_CONFIG_LOCATION = "infrared-agent.properties";
    
    private static final Logger log = LoggingFactory.getLogger(MonitorConfigImpl.class);

    private static final String KEY_ENABLE_MONITORING = "enable-monitoring";

    private static final String KEY_ENABLE_CALL_TRACING = "enable-call-tracing";

    private static final String KEY_PRUNE_THRESHOLD = "prune-threshold";

    private static final String KEY_LAST_INVOCATIONS_COUNT = "last-invocations-to-trace";

    private static final String KEY_COLLECTION_STRATEGY = "collection-strategy";

    private static final boolean DEFAULT_ENABLE_MONITORING = false;

    private static final Boolean DEFAULT_ENABLE_MONITORING_FOR_CURRENT_THREAD = Boolean.TRUE;

    private static final boolean DEFAULT_ENABLE_CALL_TRACING = false;

    private static final long DEFAULT_PRUNE_THRESHOLD = 50;

    private static final int DEFAULT_LAST_INVOCATIONS_COUNT = 5;

    private static final String DEFAULT_COLLECTION_STRATEGY = 
        "net.sf.infrared.agent.transport.impl.LoggingCollectionStrategy";

    private Properties rawProps;
    
    private Map parsedProps = new HashMap();
    
    private URL cfg;
    
    private boolean isCallFromInfraredPropertiesMBean = false;
    
    private ThreadLocal isMonitoringOnForCurrentThread = new ThreadLocal() {
        protected Object initialValue() {
            return DEFAULT_ENABLE_MONITORING_FOR_CURRENT_THREAD;
        }
    };

    public MonitorConfigImpl() {
        this(DEFAULT_CONFIG_LOCATION);
    }

    public MonitorConfigImpl(String cfgLocation) {
        rawProps = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            cfg = loader.getResource(cfgLocation);
            rawProps.load(cfg.openStream());
        } catch (Throwable th) {
            log.fatal("Error loading configuration from " + cfgLocation
                    + ". Using defaults for all configuration", th);
        }
    }
    
    MonitorConfigImpl(Properties props) {
        this.rawProps = props;
    }
    
    public boolean isMonitoringEnabled() {
    	return getProperty(KEY_ENABLE_MONITORING, DEFAULT_ENABLE_MONITORING);
    }

    public void enableMonitoring(boolean enable) {
        setProperty(KEY_ENABLE_MONITORING, enable);
    }

    public boolean isMonitoringEnabledForCurrentThread() {
        Boolean b = (Boolean) isMonitoringOnForCurrentThread.get();
        return b.booleanValue();
    }

    public void enableMonitoringForCurrentThread(boolean enable) {
        isMonitoringOnForCurrentThread.set(Boolean.valueOf(enable));
    }

    public boolean isCallTracingEnabled() {
        return getProperty(KEY_ENABLE_CALL_TRACING, DEFAULT_ENABLE_CALL_TRACING);
    }

    public void enableCallTracing(boolean enable) {
        setProperty(KEY_ENABLE_CALL_TRACING, enable);
    }

    public long getPruneThreshold() {
        return getProperty(KEY_PRUNE_THRESHOLD, DEFAULT_PRUNE_THRESHOLD);
    }

    public void setPruneThreshold(long pruneThreshold) {
        setProperty(KEY_PRUNE_THRESHOLD, pruneThreshold);
    }

    public int getNoOfLastInvocationsToBeTracked() {
        return getProperty(KEY_LAST_INVOCATIONS_COUNT, DEFAULT_LAST_INVOCATIONS_COUNT);
    }

    public void setNoOfLastInvocationsToBeTracked(int n) {
        setProperty(KEY_LAST_INVOCATIONS_COUNT, n);
    }

    public String getCollectionStrategy() {
        return getProperty(KEY_COLLECTION_STRATEGY, DEFAULT_COLLECTION_STRATEGY);
    }

    public String getProperty(String propertyName, String defaultValue) {
        return rawProps.getProperty(propertyName, defaultValue);
    }

    public void setProperty(String propertyName, String value) {
        rawProps.setProperty(propertyName, value);
        parsedProps.remove(propertyName);
    }

    /*
    private ThreadLocal threadLocalProps = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
    };*/
    
    public int getProperty(String propertyName, int defaultValue) {
        Integer in = (Integer) parsedProps.get(propertyName);
        if (in != null) {
            return in.intValue();
        }
        
        
        /*
    	Map cache = (Map) threadLocalProps.get();
    	Integer cachedProp = (Integer) cache.get(propertyName);
    	if (cachedProp != null && !isCallFromInfraredPropertiesMBean) {
    		return cachedProp.intValue();
    	}
    	isCallFromInfraredPropertiesMBean = false;*/
        
    	String value = getProperty(propertyName);

		if (value == null) {
			return defaultValue;
		}

		int i = defaultValue;
		try {
			i = Integer.parseInt(value);
			parsedProps.put(propertyName, new Integer(i));
		} catch (NumberFormatException e) {
			log.error("Failed to parse value " + value + " for property "
					+ propertyName + " to integer, using default "
					+ defaultValue);
		}
		
		return i;
	}

    public void setProperty(String propertyName, int value) {
        setProperty(propertyName, Integer.toString(value));
    }

    public long getProperty(String propertyName, long defaultValue) {
        Long lo = (Long) parsedProps.get(propertyName);
        if (lo != null) {
            return lo.longValue();
        }
        
        /*
    	Map cache = (Map) threadLocalProps.get();
    	Long cachedProp = (Long) cache.get(propertyName);
    	if (cachedProp != null &&  !isCallFromInfraredPropertiesMBean) {
    		return cachedProp.longValue();
    	}    	
    	isCallFromInfraredPropertiesMBean = false;*/
                
    	String value = getProperty(propertyName);

        if (value == null) {
            return defaultValue;
        }

        long l = defaultValue;
        try {
            l = Long.parseLong(value);
            parsedProps.put(propertyName, new Long(l));
        } catch (NumberFormatException e) {
            log.error("Failed to parse value " + value + " for property " + propertyName
                    + " to long, using default " + defaultValue);
        }
        
        return l;
    }

    public void setProperty(String propertyName, long value) {
        setProperty(propertyName, Long.toString(value));
    }

    private static final String TRUE = "true".intern();
    private static final String FALSE = "false".intern();
    public boolean getProperty(String propertyName, boolean defaultValue) {
        Boolean bo = (Boolean) parsedProps.get(propertyName);
        if (bo != null) {
            return bo.booleanValue();
        }
        
        /*
    	Map cache = (Map) threadLocalProps.get();
    	Boolean cachedProp = (Boolean) cache.get(propertyName);
    	if (cachedProp != null &&  !isCallFromInfraredPropertiesMBean) {
            return cachedProp.booleanValue();
    	}
    	
    	isCallFromInfraredPropertiesMBean = false;*/
        
    	String value = getProperty(propertyName);
        if (value == null) {
            return defaultValue;
        }
        value = value.trim().intern();
        
        if (TRUE == value) {
            parsedProps.put(propertyName, Boolean.TRUE);
            return true;
        } else if (FALSE == value) {
            parsedProps.put(propertyName, Boolean.FALSE);
            return false;
        } else {
            log.error("Failed to parse value " + value + " for property " + propertyName
                    + " to boolean, using default " + defaultValue);            
            return defaultValue;
        }
    }

    public void setProperty(String propertyName, boolean value) {
        setProperty(propertyName, Boolean.toString(value));
    }
    
    public String toString() {
        return "MonitorConfig[" + cfg +"]";
    }
    
    /*
    public void resetThreadLocalCache() {
    	Map cache = (Map) threadLocalProps.get();
    	cache.clear();
    }

    public void setIsCallFromInfraredPropertiesMBean(boolean value) {
    	isCallFromInfraredPropertiesMBean = value;
    }*/
    
    
    private String getProperty(String propertyName) {
    	String value = rawProps.getProperty(propertyName);
    	//trim the value here as the numeric values with space would cause problem later
        return (value != null) ? value.trim() : null;
    }
}
