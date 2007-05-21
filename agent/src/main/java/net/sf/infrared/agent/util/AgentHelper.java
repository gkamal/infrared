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
 * Contributor(s):   -
 *
 */
package net.sf.infrared.agent.util;

import java.io.InputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * Helper methods which are used throughout agent module
 * 
 * @author binil
 */
public class AgentHelper {
    public static final String VERSION_FILE = "version.txt";

    public static final String UNKNOW_HOST = "unknown-host";

    public static final String UNKNOW_VERSION = "unknown";

    private static Logger log = LoggingFactory.getLogger(AgentHelper.class);
    
    private static String hostName;
    private static String version;

    /**
     * Gets the host name of the machine where this agent is running.
     * Returns "unknown-host" if the hostname could not be figured out.
     */
    public String getLocalHostName() {
        if (hostName !=  null) {
            return hostName;
        }
        
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhex) {
            log.debug("Failed to figure out host name, using default " + hostName, uhex);
            hostName = UNKNOW_HOST;
        }
        return hostName;
    }

    /**
     * Gets the version information of this agent.
     * The agent jar contains a txt file - version.txt - which has this information.
     * Returns "unknown" if the version information could not be figured out.
     */
    public String getVersion() {
        if (version !=  null) {
            return version;
        }
        
        try {
            InputStream stream = getVersionInfoAsStream();
            Properties props = new Properties();
            if (stream != null) {
                props.load(stream);
                version = props.getProperty("infrared-version");
            }
        } catch (IOException ignored) {
            // should not have happened; swallow this and go with unknown version!
        }
        if ( (version == null) || (version.trim().equals("")) ) {
            version = UNKNOW_VERSION;
        }
        
        return version;
    }

    private InputStream getVersionInfoAsStream() {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(VERSION_FILE);
    }
}
