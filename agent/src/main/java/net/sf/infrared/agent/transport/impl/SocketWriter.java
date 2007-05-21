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

import org.apache.log4j.Logger;

import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ConnectException;

/**
 * Responsible for writing the Statistics details to a Socket in case of
 * Centralized Collection Strategy.
 * 
 * @author kaushal.kumar
 * @author binil.thomas
 */
public class SocketWriter {
    private static final Logger log = LoggingFactory.getLogger(SocketWriter.class);

    private static final int DEFAULT_PORT = 7777;

    private static final int DEFAULT_RECONNECTION_DELAY = 30000;

    private static final int RESET_FREQUENCY = 1;
    
    private ObjectOutputStream oos;

    private long reconnectionDelay = DEFAULT_RECONNECTION_DELAY;

    private boolean connected = false;

    private InetAddress address;

    private int port = DEFAULT_PORT;

    private int counter = 0;

    private Connector connector;

    private Socket socket;

    public SocketWriter(InetAddress address, int port) {
        this.port = port;
        this.address = address;
    }

    public SocketWriter(String host, int port) {
        this( getAddressByName(host), port );
    }

    public void connect() {
        initiateConnection();
    }

    public synchronized void disconnect() {
        if (connector != null) connector.kill();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
        cleanUp();
        connected = false;
    }

    public void write(OperationStatistics stats) {
        writeToStream(stats);
    }

    public void write(ApplicationStatistics stats) {
        writeToStream(stats);
    }

    public void setReconnectionDelay(long delay) {
        this.reconnectionDelay = delay;
    }

    public long getReconnectionDelay() {
        return reconnectionDelay;
    }

    public boolean isConnected() {
        return connected;
    }

    public String toString() {
        return "SocketWriter [address = " + address + ", port = " + port
                + (isConnected() ? "]" : "] not") + " connected";
    }

    void initiateConnection() {
        if (getReconnectionDelay() > 0 && connector == null) {
            connector = new Connector();
            connector.setDaemon(true);
            connector.setPriority(Thread.MIN_PRIORITY);
            connector.start();
            log.info("Started connector thread");
        }
    }

    void cleanUp() {
        if (oos == null) return;
        try {
            oos.close();
        } catch (IOException e) {
            log.warn("CleanUp error: Could not close ObjectOutputStream", e);
        }
    }

    void writeToStream(Serializable stats) {
        if (stats == null) return;
        if (oos == null) return;

        try {
            oos.writeObject(stats);
            if (log.isDebugEnabled()) {
                log.debug(this + " - Wrote stats");
            }
            oos.flush();

            // Failing to reset the object output stream every now and
            // then creates a serious memory leak.
            if (++counter >= RESET_FREQUENCY) {
                counter = 0;
                oos.reset();
            }
        } catch (IOException th) {
            oos = null;
            connected = false;
            log.warn("Detected problem with connection", th);
            initiateConnection();
        }
    }

    private static InetAddress getAddressByName(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (Exception ex) {
            log.error("SocketWriter - failed to get InetAddress of host " + host, ex);
            return null;
        }
    }

    class Connector extends Thread {
        // we turn off logging after a while, so that we don't fill up the logs
        private static final int MAX_TRIES_TO_LOG = 5;

        private boolean killed = false;

        private int tries = 0;

        public void run() {
            while (!killed) {
                try {
                    tries++;
                    attemptConnection();
                    log.debug("Created socket connection with collector after "
                            + tries + " attempts");
                    connected = true;
                    connector = null;
                    tries = 0;
                    break;
                } catch (IOException e) {
                    logError("Could not connect to '" + address.getHostName() + "'", e);
                }

                try {
                    sleep( getReconnectionDelay() );
                } catch (InterruptedException e) {
                    logError("Connector thread killed while sleeping",  e);
                    return;
                }
            }
        }

        public synchronized void kill() {
            killed = true;
        }

        void attemptConnection() throws ConnectException, IOException {
            socket = new Socket(address, port);
            synchronized (this) {
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
        }

        void logError(String message, Throwable th) {
            if (tries <= MAX_TRIES_TO_LOG) {
                log.error(message, th);
            }
            if (tries == MAX_TRIES_TO_LOG) {
                log.error("Connector tried " + MAX_TRIES_TO_LOG + " times to connect to collector" +
                        " but failed. Connector will keep trying, but meanwhile it will not log " +
                        "the errors. It can be assumed that the errors are the same as earlier");
            }
        }
    }
}
