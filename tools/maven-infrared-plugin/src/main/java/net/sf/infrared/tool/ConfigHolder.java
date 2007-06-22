package net.sf.infrared.tool;

import java.util.logging.Logger;

/**
 * Hold the configuration required for Infrared tool. This can be called from any part
 * and thus no need to pass configuration around the code. 
 * @author chetanm
 */
public class ConfigHolder {
	private static final Logger logger = Logger.getLogger(ConfigHolder.class.getName());
	private static Config _instance = null;
	
	/**
	 * As it would be called in maven,ant thus in single threaded application.
	 * So need to worry about sync
	 * @return
	 */
	public static Config getConfig(){
		if(_instance == null){
			logger.fine("Config is not set so creating a new config");
			_instance = new Config();
		}
		return _instance;
	}
	
	public static void setConfig(Config conf){
		assert conf != null : "Configuration cannot be set to null";
		_instance = conf;
	}
	
	public static void clear(){
		_instance = null;
	}

}
