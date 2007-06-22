package net.sf.infrared.tool.mojo;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.maven.plugin.logging.Log;

public class MavenMessageHandler  extends Handler{
	private final Log log;
	
	public MavenMessageHandler(Log log,String logLevel){
		this.log = log;
		setLoggingLevel(logLevel);
	}

	private void setLoggingLevel(String logLevel) {
		if("quiet".equalsIgnoreCase(logLevel)){
			setLevel(Level.WARNING);
		}else if("debug".equalsIgnoreCase(logLevel)){
			setLevel(Level.INFO);
		}else if("verbose".equalsIgnoreCase(logLevel)){
			setLevel(Level.FINER);
		}else{
			setLevel(Level.INFO);
		}
	}

    public synchronized void publish(LogRecord record) {
        String message = record.getMessage();
        Throwable t = record.getThrown();
        int logLevel = getLevel().intValue();
    	int recLevel = record.getLevel().intValue();
    	if(recLevel >= logLevel){
    		if(recLevel >= Level.SEVERE.intValue()){
    			if( t == null){
    				log.error(message);
    			} else{
    				log.error(message,t);
    			}
    		}else if (recLevel >= Level.WARNING.intValue()){
    			if( t == null){
    				log.warn(message);
    			} else{
    				log.warn(message,t);
    			}
    		}else if (recLevel >= Level.INFO.intValue()){
    			if( t == null){
    				log.info(message);
    			} else{
    				log.info(message,t);
    			}
    		}else{
    			if( t == null){
    				log.debug(message);
    			} else{
    				log.debug(message,t);
    			}
    		}
    	}
    }

	public void flush() {
    }

    public void close() {
    }
}
