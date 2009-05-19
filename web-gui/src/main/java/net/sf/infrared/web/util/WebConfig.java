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

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

public class WebConfig {

    public static final String COLLECTOR_PERSIST_INTERVAL = "persist-interval";
    public static final String WEB_COLOR_THRESHOLD = "web.color-threshold";
    public static final String WEB_NUM_OF_SQL_QUERIES = "web.num-of-sql-queries";
    
    public static final String DEFAULT_WEB_CONFIG_LOCATION = "infrared-web.properties";
    
    public static final String DEFAULT_COLLECTOR_PERSIST_INTERVAL = "600000";
    public static final String DEFAULT_WEB_COLOR_THRESHOLD = "25";
    public static final String DEFAULT_NUM_OF_LAST_INVOCATIONS = "5";
    public static final String DEFAULT_NUM_OF_SQL_QUERIES = "5";
    
    public static Logger log = LoggingFactory.getLogger(WebConfig.class);
    private static PropertyUtil propertyUtil;

    static{
        propertyUtil = new PropertyUtil(DEFAULT_WEB_CONFIG_LOCATION);                
    }
    
    public static long getPersistInterval(){
        String interval = propertyUtil.getProperty(COLLECTOR_PERSIST_INTERVAL, 
                                                            DEFAULT_COLLECTOR_PERSIST_INTERVAL);
        return Long.parseLong(interval);
    }
    
    public static int getColorThreshold(){
        String threshold = propertyUtil.getProperty(WEB_COLOR_THRESHOLD, 
                                                                    DEFAULT_WEB_COLOR_THRESHOLD);
        return Integer.parseInt(threshold);
    }
    
    public static int getNumOfSqlQueries(){
        String sqlQueries = propertyUtil.getProperty(WEB_NUM_OF_SQL_QUERIES, 
                                                                       DEFAULT_NUM_OF_SQL_QUERIES);
        return Integer.parseInt(sqlQueries);
    }
}
