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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.base.model;

import java.io.Serializable;
import java.util.List;

/**
 * Represents what is being executed. InfraRED tracks timing information regarding various 
 * executions - apis, sqls, jsps etc - within an application. The ExecutionContext identifies what
 * each of these executions are.
 * 
 * <p>
 * ExecutionContexts form a tree. This is done to accomodate some executions which are grouped
 * by nature. For instance, one can have a SqlExecutionContext which identifies all activity 
 * involving a given SQL string, but these might need further grouping into PREPARE, EXECUTE etc.
 * In this case we have SqlPrepareContext and SqlExecutionContext which can be children of 
 * a SqlContext. 
 *  
 * @author prashant.nair
 */
public interface ExecutionContext extends Serializable {
    ExecutionContext getParent();

    String getLayer();

    List getChildren();

    String getName();

    String toString();

    void addChild(ExecutionContext ctx);
}
