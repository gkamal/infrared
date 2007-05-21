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
package net.sf.infrared.agent.util;

/**
 * Object wrapper for int which is mutable. Used in situations where an Object
 * is required (map key or value) and using the Integer would lead to
 * unnecessary creation of temporary objects
 * 
 * @author kamal.govindraj
 */
public class MutableInteger {
    private int value;

    public MutableInteger() {
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    public MutableInteger(Integer value) {
        this.value = value.intValue();
    }

    public int intValue() {
        return value;
    }

    public MutableInteger add(int addend) {
        value += addend;
        return this;
    }

    public MutableInteger subtract(int subtractant) {
        value -= subtractant;
        return this;
    }

    public MutableInteger increment() {
        value++;
        return this;
    }

    public MutableInteger decrement() {
        value--;
        return this;
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isPositive() {
        return value >= 0;
    }

    public boolean isNegative() {
        return value < 0;
    }

    public boolean isEqual(int value) {
        return this.value == value;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof MutableInteger)) {
            return false;
        }
        MutableInteger rhs = (MutableInteger) object;
        return this.value == rhs.value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return Integer.toString(value);
    }
}
