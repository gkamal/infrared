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
 * Original Author:  Ceki G&uuml;lc&uuml
 * Contributor(s):   -;   Moses Hohman <mmhohman@rainbow.uchicago.edu>, binil.thomas (Tavant Technologies)
 *
 */
package net.sf.infrared.collector.impl.transport;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.StatisticsRepository;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Read {@link LoggingEvent} objects sent from a remote client using Sockets
 * (TCP). These logging events are logged according to local policy, as if they
 * were generated locally.
 * 
 * <p>
 * For example, the socket node might decide to log events to a local file and
 * also resent them to a second socket node.
 * 
 */
public class AgentThread extends Thread {
    private Socket socket;

    /* The listener thread that spawned this thread */
    private AgentListener parentListener;

    private ObjectInputStream ois;

    private StatisticsRepository statsRepository;

    private static Logger logger = LoggingFactory.getLogger(AgentThread.class.getName());

    public AgentThread(Socket socket, AgentListener listener, StatisticsRepository statsRepository) {
        this.statsRepository = statsRepository;
        this.socket = socket;
        this.parentListener = listener;
        setName("InfraRED-Agent-Communication:" + socket);
        try {
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            logger.error("Could not open stream to read from " + socket, e);
        }
    }

    public void run() {
        try {
            while (true) {
                ApplicationStatistics newStats = (ApplicationStatistics) ois.readObject();
                statsRepository.addStatistics(newStats);
                if (logger.isDebugEnabled()) {
                    logger.debug("Received statistics from " + newStats.getApplicationName() + "#"
                            + newStats.getInstanceId());
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected exception while reading from socket; closing connection", e);
        } finally {
            shutdown();
        }
    }

    public boolean isOpen() {
        return !socket.isClosed();
    }

    public void shutdown() {
        try {
            ois.close();
        } catch (Exception e) {
            logger.info("Could not close connection", e);
        }
        try {
            socket.close();
        } catch (Exception e) {
            logger.info("Could not close connection", e);
        }
        parentListener.removeAgent(this);
    }

    public String toString() {
        return "AgentThread[" + socket.getLocalPort() + "] --> " + socket.getInetAddress()
                + ":" + socket.getPort();
    }
}
