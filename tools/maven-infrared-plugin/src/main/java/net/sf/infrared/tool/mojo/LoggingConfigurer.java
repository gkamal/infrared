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
 */
package net.sf.infrared.tool.mojo;

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Infrared runtime configurer for java.util.Logging.
 *
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class LoggingConfigurer {
    private LoggingConfigurer() {
    }

    /**
     * Configures logging messages to use specified handler
     */
    public static void configure(Handler handler) {
        final Logger l2 = Logger.getLogger("net.sf.infrared");
        l2.setLevel(handler.getLevel());
        l2.setUseParentHandlers(false);
        l2.addHandler(handler);
    }

    public static void remove(Handler handler) {
        final Logger l2 = Logger.getLogger("net.sf.infrared");
        l2.removeHandler(handler);
    }
}
