/*
 *
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
 * Contributor(s):   prashant.nair;
 *
 */
package net.sf.infrared.web.treecontrolmodel;

import net.sf.infrared.base.model.AggregateExecutionTime;

public interface NodeToStringConverter {
    public String toString(AggregateExecutionTime aggApiTime);

    public String getHref(AggregateExecutionTime aggApiTime);

    public boolean isContextRelative();
	

}
