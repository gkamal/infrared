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
 * Contributor(s):   binil.thomas;
 *
 */
package net.sf.infrared.base.util;

import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

/**
 * LoggingFactory implementation
 * 
 * @author kamal.govindraj
 * @author binil.thomas
 */
public class LoggingFactory {
    public static final String DEBUG_KEY = "infrared.debug";

    public static final String INFO_KEY = "infrared.info";

    public static final String LOG4J_CONF = "infrared-log4j.xml";
    
    public static final String DEFAULT_CONF = "default-infrared-log4j.xml";

    private static URL log4jUrl = null;
    
    private static boolean isLoggingConfigured = true;

    private static boolean isUsingDefault = false;
    
//    static {
//        try {
//            log4jUrl = Thread.currentThread().getContextClassLoader().getResource(LOG4J_CONF);
//            if (log4jUrl == null) {
//                log4jUrl = Thread.currentThread().getContextClassLoader().getResource(DEFAULT_CONF);
//                isUsingDefault = true;
//            }
//            try {
//                Class.forName("net.sf.infrared.org.apache.log4j.LogManager");
//            } catch (ClassNotFoundException ignored) {
//            }
//            DOMConfigurator.configure(log4jUrl);
//            isLoggingConfigured = true;
//        } catch (RuntimeException ex) {
//            System.out.println("[InfraRED] Problems configuring logging system");
//            ex.printStackTrace();
//        }
//    }

    public static Logger getLogger(Class clazz) {
        Logger wrapper = new LoggerDecorator(Logger.getLogger(clazz));
        return wrapper;
    }

    public static Logger getLogger(String loggerName) {
    	Logger wrapper = new LoggerDecorator(Logger.getLogger(loggerName));
        return wrapper;
    }
    
    public static URL getLoggingConfiguration() {
        return log4jUrl;
    }
    
    public static boolean isLoggingConfigured() {
        return isLoggingConfigured;
    }
    
    public static boolean isDefultLoggingUsed() {
        return isUsingDefault;
    }
    
    public static boolean isDebugLoggingEnabled() {
        return isInfoLoggingEnabled() || Boolean.getBoolean(LoggingFactory.DEBUG_KEY);
    }

    public static boolean isInfoLoggingEnabled() {
        return Boolean.getBoolean(LoggingFactory.INFO_KEY);
    }
}

class LoggerDecorator extends Logger {
	private Logger delegate;
	
	private static boolean debug = LoggingFactory.isDebugLoggingEnabled();

    private static boolean info = LoggingFactory.isInfoLoggingEnabled();

	public LoggerDecorator(Logger delegate) {
		super(delegate.getName());
		this.delegate = delegate;		
	}

	public synchronized void addAppender(Appender arg0) {
		delegate.addAppender(arg0);		
	}

	public void assertLog(boolean arg0, String arg1) {
		delegate.assertLog(arg0, arg1);
	}

	public void callAppenders(LoggingEvent arg0) {
		delegate.callAppenders(arg0);
	}

	public void debug(Object arg0, Throwable arg1) {
		delegate.debug(arg0, arg1);
	}

	public void debug(Object arg0) {
		delegate.debug(arg0);
	}

	public void error(Object arg0, Throwable arg1) {
		delegate.error(arg0, arg1);
	}

	public void error(Object arg0) {
		delegate.error(arg0);
	}

	public void fatal(Object arg0, Throwable arg1) {
		delegate.fatal(arg0, arg1);
	}

	public void fatal(Object arg0) {
		delegate.fatal(arg0);
	}

	public boolean getAdditivity() {
		return delegate.getAdditivity();
	}

	public synchronized Enumeration getAllAppenders() {
		return delegate.getAllAppenders();
	}

	public synchronized Appender getAppender(String arg0) {
		return delegate.getAppender(arg0);
	}


	public LoggerRepository getLoggerRepository() {
		return delegate.getLoggerRepository();
	}

	public ResourceBundle getResourceBundle() {
		return delegate.getResourceBundle();
	}

	public void info(Object arg0, Throwable arg1) {
		delegate.info(arg0, arg1);
	}

	public void info(Object arg0) {
		delegate.info(arg0);
	}

	public boolean isAttached(Appender arg0) {
		return delegate.isAttached(arg0);
	}

	public boolean isDebugEnabled() {
		return debug;
	}

	public boolean isEnabledFor(Priority arg0) {
		return delegate.isEnabledFor(arg0);
	}

	public boolean isInfoEnabled() {
		return info;
	}

	public void l7dlog(Priority arg0, String arg1, Object[] arg2, Throwable arg3) {
		delegate.l7dlog(arg0, arg1, arg2, arg3);
	}

	public void l7dlog(Priority arg0, String arg1, Throwable arg2) {
		delegate.l7dlog(arg0, arg1, arg2);
	}

	public void log(Priority arg0, Object arg1, Throwable arg2) {
		delegate.log(arg0, arg1, arg2);
	}

	public void log(Priority arg0, Object arg1) {
		delegate.log(arg0, arg1);
	}

	public void log(String arg0, Priority arg1, Object arg2, Throwable arg3) {
		delegate.log(arg0, arg1, arg2, arg3);
	}

	public synchronized void removeAllAppenders() {
		delegate.removeAllAppenders();
	}

	public synchronized void removeAppender(Appender arg0) {
		delegate.removeAppender(arg0);
	}

	public synchronized void removeAppender(String arg0) {
		delegate.removeAppender(arg0);
	}

	public void setAdditivity(boolean arg0) {
		delegate.setAdditivity(arg0);
	}

	public void setLevel(Level arg0) {
		delegate.setLevel(arg0);
	}

	public void setResourceBundle(ResourceBundle arg0) {
		delegate.setResourceBundle(arg0);
	}

	public void warn(Object arg0, Throwable arg1) {
		delegate.warn(arg0, arg1);
	}

	public void warn(Object arg0) {
		delegate.warn(arg0);
	}	
}
