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
 * Original Author:  kaushal.kumar (Tavant Technologies)
 * Contributor(s):   binil.thomas;
 *
 */
package net.sf.infrared.agent.transport.impl;

import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * Forwarder which uses a socket to send statistics to the remote infrared web
 * application
 * 
 * @author kaushal.kumar
 * @author binil.thomas
 */
public class SocketForwarder implements Forwarder {
    private SocketWriter writer = null;

    private Logger logger = LoggingFactory.getLogger(SocketForwarder.class);

    public SocketForwarder(String hostName, int portNo) {
        writer = new SocketWriter(hostName, portNo);
    }

    public void init(boolean suspended) {
        if (!suspended) {
            writer.connect();
        }
    }

    public void forward(ApplicationStatistics stats) {
        if (stats.hasStatistics()) {
            writer.write(stats);
        } else {
            logger.info("Ignoring forward as there is no stats to send");
        }
    }

    public void forward(OperationStatistics stats) {
        writer.write(stats);
    }

    public void suspend() {
        throw new UnsupportedOperationException("suspend/resume not implemented at this point");
    }

    public void resume() {
        throw new UnsupportedOperationException("suspend/resume not implemented at this point");
    }

    public void destroy() {
        writer.disconnect();
    }
}
