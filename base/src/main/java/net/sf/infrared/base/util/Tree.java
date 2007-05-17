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

import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * Implementation of a Tree data Structure. A node in this tree can represent
 * any data object
 * 
 * @author kamal.govindraj
 */
public class Tree implements Serializable {
    private static final Logger log = LoggingFactory.getLogger(Tree.class);
    
    private TreeNode root = null;

    /**
     * Creates a Tree with null root.
     */
    public Tree() {
        this.root = null;
    }

    /**
     * Constructor that creates a Tree with a specified Node as root.
     * 
     * @param root -
     *            root node
     */
    public Tree(TreeNode root) {
        setRoot(root);
    }

    /**
     * Gets the root node of this tree.
     * 
     * @return root node
     */
    public TreeNode getRoot() {
        return this.root;
    }

    public void setRoot(TreeNode root) {
        if (this.root != null) {
            this.root.setDepth(-1);
            this.root.setPostion(-1);
            if (log.isDebugEnabled()) {
                log.debug("Replacing root node of tree");
            }
        }
        this.root = root;
        if (this.root != null) {
            this.root.setDepth(0);
            this.root.setPostion(0);
        }
    }

    public void setDepth() {
        root.setDepth(0);
    }

    public void traverseBreadthFirst(NodeVisitor visitor) {
        if (log.isDebugEnabled()) {
            log.debug("Entering method traverseBreadthFirst");
        }
        visitor.beginTraversal();
        if (this.root != null) {
            visitor.visit(root);
            visitor.goingDown();
            this.root.traverseBreadthFirst(visitor);
            visitor.climbingUp();
        }
        visitor.endTraversal();
    }
    

    public TreeNode find(Object value) {
        if (this.root == null) {
            return null;
        }
        if (this.root.getValue() == value) {
            return this.root;
        } else {
            return this.root.find(value);
        }
    }

    public String toString() {
        return root.toString();
    }
}
