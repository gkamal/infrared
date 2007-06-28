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
 */
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
