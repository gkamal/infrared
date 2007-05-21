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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent.transport.impl;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.infrared.agent.util.MutableInteger;

public class SocketWriterTest extends TestCase {
    public void testIfAttemptsAreMadeToReEstablishConnections() throws Exception {
        final int port = 9999;

        final MutableInteger tries = new MutableInteger(0);

        // first, setup a server which accpets connections and closes them immideately
        // this way, the SocketWriter will never be able to maintain a steady connection
        // to the server
        TestServer connectionRejectingServer = new TestServer() {
            public int getPort() { return port; }
            public void processConnection(Socket con) {
                tries.increment();
                try { con.close(); } catch (IOException ioex) { }
            }
        };
        connectionRejectingServer.setDaemon(true);
        connectionRejectingServer.start();

        Thread.sleep(100); // wait a bit
        assertEquals("Initially we should not have had any program connecting to the " +
                "connectionRejectingServer", 0, tries.intValue());

        // let the fixture connect to the server we setup
        final SocketWriter fixture = new SocketWriter("localhost", port) {
            //set up the fixture to try reconnecting every millisecond
            public long getReconnectionDelay() { return 1; }
        };
        fixture.connect();
        Thread.sleep(10);

        // have a thread constantly writing to the socket using our fixture
        Thread writer = new Thread() {
            public void run() {
                while (true) {
                    fixture.writeToStream("Test");
                    try { Thread.sleep(10); } catch (InterruptedException ex) { }
                }
            }
        };
        writer.setDaemon(true);
        writer.start();

        Thread.sleep(100); // wait a bit
        //assertTrue("SocketWriter should have tried to reconnect to the connectionRejectingServer",
        //        tries.intValue() > 1);

        connectionRejectingServer.kill(); // cleanup
    }

    public void testStartAndShutdown() throws Exception {
        final int port = 9999;

        TestServer server = new TestServer() {
            public int getPort() { return port; }
            public void processConnection(Socket con) { }
        };
        server.setDaemon(true);
        server.start();
        Thread.sleep(10); // wait a bit, to get the server started

        final SocketWriter fixture = new SocketWriter("localhost", port) {
            //set up the fixture to try reconnecting every millisecond
            public long getReconnectionDelay() { return 1; }
        };
        assertFalse(fixture.isConnected());

        fixture.connect();
        Thread.sleep(100);
        assertTrue(fixture.isConnected());

        fixture.disconnect();
        Thread.sleep(10);
        assertFalse(fixture.isConnected());

        server.kill();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SocketWriterTest.class);
    }
}

abstract class TestServer extends Thread {
    private volatile boolean killed = false;
    private ServerSocket serverSocket;
    public void run() {
        try {
            serverSocket = new ServerSocket(getPort());
            while (!killed) {
                if (! serverSocket.isClosed()) {
                    Socket con = serverSocket.accept();
                    processConnection(con);
                }
            }
        } catch (IOException io) {
        }
    }
    public void kill() {
        killed = true;
        try {
            if (serverSocket != null){
            	serverSocket.close();
            }
        } catch (IOException ex) {
        }
    }
    public abstract int getPort();
    public abstract void processConnection(Socket con);
}