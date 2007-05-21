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
 * Original Author: binil.thomas (Tavant Technologies) 
 * Contributor(s):  -;
 *
 */
package net.sf.infrared.agent.transport.impl;

import org.apache.log4j.Logger;

import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

/**
 * 
 * @author binil.thomas
 */
public class LoggingForwarder implements Forwarder {
    private static final Logger log = LoggingFactory.getLogger(LoggingForwarder.class);

    public void destroy() {
    }

    public void forward(ApplicationStatistics stats) {
        log.fatal("\n\nForwaring:- \n" + stats);
    }

    public void forward(OperationStatistics stats) {
    }

    public void init(boolean suspended) {
    }

    public void resume() {
    }

    public void suspend() {
    }
}
