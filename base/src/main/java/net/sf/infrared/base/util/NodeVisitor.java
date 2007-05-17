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
package net.sf.infrared.base.util;

/**
 * Interface that defines method for Tree Node Traversal.
 * 
 * @author kamal.govindraj
 */
public interface NodeVisitor {
    /**
     * Visiting a node of the tree
     * 
     * @param node
     */
    void visit(TreeNode node);

    /**
     * Going down to the next level
     */
    void goingDown();

    /**
     * Retracing to the previous level
     */
    void climbingUp();

    /**
     * Called just before the traversal begins
     */
    void beginTraversal();

    /**
     * Called after the traversal is complete
     */
    void endTraversal();
}
