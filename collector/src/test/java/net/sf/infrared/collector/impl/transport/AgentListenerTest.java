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
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.collector.impl.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.TestCase;

public class AgentListenerTest extends TestCase {
    public void testOddFailure() {
        final int port = 10;
        AgentListener fixture = null;
        try {
            fixture = new AgentListener(port) {
                ServerSocket createServerSocket(int port) throws IOException {
                    return new MockServerSocket(9999, this, new boolean[] { true, true, false,
                            true, true });
                }

                void resetListener() {
                    fail("for an odd failure, it should not attempt to reset listener");
                }

                void handleAgentConnection(Socket socket) {
                    // ignore
                }
            };
        } catch (IOException e) {
        }

        fixture.run();
    }

    boolean pass = false;

    public void testConsistentFailure() {
        final int port = 10;
        AgentListener fixture = null;
        try {
            fixture = new AgentListener(port) {
                ServerSocket createServerSocket(int port) throws IOException {
                    return new MockServerSocket(9999, this, new boolean[] { true, false, false,
                            false, false, false, false, true });
                }

                void resetListener() {
                    pass = true;
                }

                void handleAgentConnection(Socket socket) {
                    // ignore
                }
            };
        } catch (IOException e) {
        }

        fixture.run();
        assertTrue("for consistent failures, the listener should be reset", pass);
    }
}

class MockServerSocket extends ServerSocket {
    int port;

    boolean[] accepts;

    int invocationCount = 0;

    AgentListener fixture = null;

    public MockServerSocket(int port, AgentListener fixture, boolean[] accepts) throws IOException {
        super(port);
        this.port = port;
        this.accepts = accepts;
        this.fixture = fixture;
    }

    public Socket accept() throws IOException {
        invocationCount++;
        if (invocationCount >= accepts.length) {
            fixture.requestShutdown();
        }
        if (accepts[invocationCount - 1]) {
            return null;
        } else {
            throw new IOException();
        }
    }
}
