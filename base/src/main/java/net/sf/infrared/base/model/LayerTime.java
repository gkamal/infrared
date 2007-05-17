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
package net.sf.infrared.base.model;

import org.apache.log4j.Logger;
import net.sf.infrared.base.util.LoggingFactory;

import java.io.Serializable;

/**
 * This class contains the total time taken for executions in each layer. Examples of layers 
 * are ejb layer, web layer, Remote Call, Entity, etc. A layer can be defined specific to an 
 * application that is being instrumented.
 * 
 * @author kamal.govindraj
 */
public class LayerTime implements Cloneable, Serializable {
    private static final Logger log = LoggingFactory.getLogger(LayerTime.class);

    private String layer;

    private long time;

    public LayerTime(String layer) {
        this.layer = layer;
    }

    public String getLayer() {
        return this.layer;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void addToTime(long time) {
        this.time += time;
    }

    public void subtractFromTime(long time) {
        this.time -= time;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Error CloneNotSupportedException should never be thrown", e);
            return null; // unreachable statement
        }
    }

    public String toString() {
        return layer + " = " + time;
    }
}
