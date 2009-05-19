/*
 *
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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   
 *
 */

package net.sf.infrared.web.util;

import java.net.URL;
import java.util.Properties;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

public class PropertyUtil {
    
    private Properties prop;
    private URL config;
    public static Logger log = LoggingFactory.getLogger(PropertyUtil.class);

    public PropertyUtil(String propertyFile){
        try {
            prop = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            config = loader.getResource(propertyFile);
            prop.load(config.openStream());
        }
        catch (Exception e) {
            log.error("Unable to load web configuration , will use default configuration");
        }        
    }
    
    public String getProperty(String property , String defaultValue){
        if(prop.getProperty(property) == null){
            log.debug("The property " + property + " is not defined. " +
                                    "Returning default value " + defaultValue);
            return defaultValue;
        }
        else{
            return prop.getProperty(property);
        }
    }


}
