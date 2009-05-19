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
 * Contributor(s):   binil.thomas (Tavant Technologies);
 *
 * $Id: InfraRedListener.java,v 1.1 2005/09/20 16:22:06 kamal_gs Exp $
 *
 * Changes:
 * --------
 *
 */

package net.sf.infrared.collector.impl.transport;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.CollectorConfig;
import net.sf.infrared.collector.StatisticsRepository;

import org.apache.log4j.Logger;

/**
 * This thread listens on a server socket for connections from remote agents.
 * This thread is initiated by the InfraRedListenerServlet. <br>
 * Once an agent connects to the server socket, a new thread
 * (InfraREDSocketNode) is spawned and further communication with that agent
 * happens on that thread.
 */
public class AgentListener extends Thread {
    private static final int MAX_RETRIES = 5;

    private static final int WAIT_IN_MINS = 10;

    private static final Logger log = LoggingFactory.getLogger(AgentListener.class.getName());

    private int port;

    private ServerSocket server;

    private volatile boolean shutdownRequested = false;

    private List agents = new ArrayList();

    private int failCount = 0;

    private StatisticsRepository statsRepository;

    /**
     * Creates a new listener. The listener needs to be started using the
     * start() method
     * 
     * @throws IOException
     *             if there is any errors in creating a listener on the
     *             specified port
     */
    public AgentListener(CollectorConfig cfg, StatisticsRepository statsRepository)
            throws IOException {
        this(cfg.getAgentListenPort());
        this.statsRepository = statsRepository;
        setName("InfraRED-Agent-Listener");
        setDaemon(true);
    }
    
    AgentListener(int port) throws IOException {
        server = createServerSocket(port);
    }

    /**
     * Waits for agents to connect. When a connection is obtained, further
     * communication with that agent is spawned on a new thread. <br>
     * If the listener fails, the following strategy is employed to recover:
     * <ul>
     * <li> 1) Upto MAX_RETIES consecutive failures are ignored </li>
     * <li> 2) On the next failure, attempt to create a new listener is made
     * </li>
     * <li> 3) If a new listener is created, operations resume as normal </li>
     * <li> 4) If the new listener can't be created, the thread retries every
     * WAIT_IN_MINS mins until it eventually can create new listener </li>
     * </ul>
     * The listener is stopped using the {@link #requestShutdown()} method
     */
    public void run() {
        log.debug("Starting the AgentListener for the Collector");
        while (!shutdownRequested) {
            Socket socket = null;
            try {
                socket = server.accept();
                handleAgentConnection(socket);
            } catch (Exception ex) {
                handleListenerFailure(ex);
            }
        }
        shutdown();
    }

    public void removeAgent(AgentThread agent) {
        if (agent.isOpen()) {
            throw new IllegalStateException("The agent that is asked to be removed is still open");
        }
        // when this thread is shutting down gracefully, the agents list is
        // being iterated;
        // in this case, remove() calls should be ignored (to avoid to
        // ConcurrentModificationException)
        // when the thread has shutdown gracefully, the agents list would be
        // empty, so
        // remove() calls can anyway be ignored.
        if (!shutdownRequested) {
            agents.remove(agent);
        }
    }

    public void requestShutdown() {
        shutdownRequested = true;
        log.debug("Requested to be shutdown gracefully");
    }

    public boolean isListenerRunning() {
        return isAlive() && !shutdownRequested;
    }

    public List getAllActiveAgentConnections() {
        return Collections.unmodifiableList(agents);
    }

    void shutdown() {
        try {
            server.close();
        } catch (IOException ignore) {
            log.warn("Error closing server socket while shutting down listener; ignored", ignore);
        }
        closeAllAgentConnections();
    }

    void closeAllAgentConnections() {
        for (Iterator i = agents.iterator(); i.hasNext();) {
            AgentThread agent = (AgentThread) i.next();
            agent.shutdown();
        }
    }

    void handleAgentConnection(Socket socket) {
        if (log.isDebugEnabled()) {
            log.debug("Received an agent connection on socket " + socket);
        }
        failCount = 0;
        try {
            AgentThread agent = new AgentThread(socket, this, statsRepository);
            agent.start();
            agents.add(agent);
            if (log.isDebugEnabled()) {
                log.debug("Initiated communication with an agent on socket " + socket);
            }
        } catch (Exception e) {
            // TODO: Think thru this
            log.error(e);
            log.error(this.getClass().getClassLoader());
        }
    }

    void handleListenerFailure(Exception ex) {
        if (failCount < MAX_RETRIES) {
            log.error("Listener failed while accepting connection; ignoring for "
                    + "now and continuing accepting further connections", ex);
            failCount++;
            return;
        }
        log.error("Listener failed for " + MAX_RETRIES + " consecutive times while accepting "
                + "connection; resetting listener", ex);
        resetListener();
    }

    void resetListener() {
        boolean stopLogging = false;
        while (true) {
            // wait a bit before recreating the socket;
            // you know, the condition that caused the failure
            // might need some time to get fixed - lets wait
            waitABit();
            boolean success = tryCreateANewServerSocket(stopLogging);
            if (success) {
                log.error("Successfully recreated listener");
                if (stopLogging) {
                    log.error("Now that listener is recreated, normal logging is resumed");
                }
                return; // successfully reset listener
            } else {
                stopLogging = true;
            }
        }
    }

    boolean tryCreateANewServerSocket(boolean dontLog) {
        try {
            server = createServerSocket(port);
            return true;
        } catch (BindException bindex) {
            if (!dontLog) {
                log.error("Attempt to recreate the listener failed; looks like the port '" + port
                        + "' is in use by some other application. Try to shutdown that other"
                        + "application; InfraRED will attempt to reconnect again every "
                        + WAIT_IN_MINS
                        + " mins - meanwhile, no further error logs will be produced", bindex);
            }
            return false;
        } catch (IOException e) {
            if (!dontLog) {
                log.error("Attempt to recreate the listener failed. Please try to fix this; "
                        + "InfraRED will attempt to reconnect again in every " + WAIT_IN_MINS
                        + " mins - meanwhile, no further error logs will be produced", e);
            }
            return false;
        }
    }

    void waitABit() {
        try {
            Thread.sleep(WAIT_IN_MINS * 60 * 1000);
        } catch (InterruptedException ignored) {
        }
    }

    ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
}
