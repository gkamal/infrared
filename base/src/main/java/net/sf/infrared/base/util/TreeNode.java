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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.infrared.base.model.AggregateExecutionTime;

import org.apache.log4j.Logger;

/**
 * Implementation of a TreeNode that represents a single node in a Tree.
 * 
 * @author kamal.govindraj
 */
public class TreeNode implements Serializable, Cloneable {
    private static final Logger log = LoggingFactory.getLogger(TreeNode.class);
    
    private Object value = null;

    private TreeNode parentNode = null;

    private List childNodes = new ArrayList();

    // Indicates the depth/level at which the node appears in a
    // tree. Will only be set when the Node is part of a tree
    private int depth = -1;

    // Indicates the postion the node occupies in the children
    // list of the parent node. Will only be set when the Node is part
    // of a tree.
    private int position = -1;

    /**
     * Default constructor - required for handling mapping to XML
     */
    public TreeNode() {

    }
    
    /**
     * Contruct a new TreeNode.
     * 
     * @param value -
     *            value contained in this node
     */
    private TreeNode(Object value) {
        this.value = value;
    }

    /**
     * Factory method to create tree nodes.
     * 
     * @param value
     * @return a new tree node object
     */
    public static TreeNode createTreeNode(Object value) {
        return new TreeNode(value);
    }

    /**
     * Get the value associated with this TreeNode.
     * 
     * @return associated value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Return the list of child nodes. The returned list is a unmodifiyable copy
     * of the list of childnodes this TreeNode maintains.
     * 
     * @return returns list of TreeNodes
     */
    public List getChildren() {
        return Collections.unmodifiableList(this.childNodes);
    }
    
    public int getNumberOfChildren(){
    	return this.childNodes.size();
    }

    /**
     * Add a new child node to the end of the list.
     * 
     * @param child -
     *            child node shouldn't be null
     */
    public void addChild(TreeNode child) {
        if (child == null) {
            throw new IllegalArgumentException("Child node can't be null");
        }

        if (child.getParent() != null) {
            throw new IllegalArgumentException("Child node is already a part of some node");
        }
        // Set the parent node
        this.childNodes.add(child);
        child.setParent(this);
        if (depth != -1) {
            child.setDepth(depth + 1);
        }
        child.setPostion(this.childNodes.size() - 1);
    }

    public boolean removeChild(TreeNode child) {
        if (child == null) {
            throw new IllegalArgumentException("childnode can't be null");
        }
        int index = this.childNodes.indexOf(child);
        if (index != -1) {
            childNodes.remove(index);
            child.setParent(null);
            child.setDepth(-1);
            child.setPostion(-1);
            adjustPosition(index);
        } else {
            log.error("Child " + child + "not found in the list of children for node " + this);
            return false;
        }
        return true;
    }

    /**
     * This method is called when an child is removed to adjust the position
     * attribute of subsequent nodes
     * 
     * @param index
     */
    private void adjustPosition(int index) {
        if (index < childNodes.size()) {
            for (Iterator itr = childNodes.listIterator(); itr.hasNext();) {
                TreeNode node = (TreeNode) itr.next();
                node.setPostion(node.getPosition() - 1);
            }
        }
    }

    /**
     * Set the parent for this node
     * 
     * @param parentNode
     */
    public void setParent(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Returns the parent node
     * 
     * @return parent node
     */
    public TreeNode getParent() {
        return this.parentNode;
    }

    /**
     * traverse the tree (starting at this node) breadth first
     * 
     * @param visitor
     */
    public void traverseBreadthFirst(NodeVisitor visitor) {
        for (Iterator itr = this.childNodes.iterator(); itr.hasNext();) {
            visitor.visit((TreeNode) itr.next());
        }
        if (this.childNodes.size() > 0) {
            visitor.goingDown();
            for (Iterator itr = this.childNodes.iterator(); itr.hasNext();) {
                ((TreeNode) itr.next()).traverseBreadthFirst(visitor);
            }
            visitor.climbingUp();
        }
    }

    public TreeNode find(Object value) {
        for (Iterator itr = this.childNodes.iterator(); itr.hasNext();) {
            TreeNode currNode = (TreeNode) itr.next();
            if (currNode.getValue().equals(value)) {
                return currNode;
            }
            TreeNode foundNode = currNode.find(value);
            if (foundNode != null) {
                return foundNode;
            }
        }
        return null;
    }

    /**
     * Returns the depth at which this node appears in a tree. Will return -1 if
     * the tree is not part of a tree
     * 
     * @return
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Returns the position the node occupies among the children of the parent
     * node. Will return -1 if the tree is not part of a tree
     * 
     * @return
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Set the depth at which this node appears in a tree This is package scope
     * as it can be set only by the parent node and tree
     * 
     * @param depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
        // Adjust the depth of the children node
        for (Iterator itr = childNodes.iterator(); itr.hasNext();) {
            TreeNode childNode = (TreeNode) itr.next();
            if (depth != -1) {
                childNode.setDepth(depth + 1);
            } else {
                childNode.setDepth(-1);
            }
        }
    }

    /**
     * Set the position the node occupies among the children of the parent node.
     * Package scope as it can be set only by the parent node & Tree
     * 
     * @param position
     */
    void setPostion(int position) {
        this.position = position;
    }

    public void addChildren(List children) {
        childNodes.addAll(children);
    }

    public String toString() {
        String str = "";

        for (int i = 0; i < getDepth(); i++) {
            str = str + "--";
        }

        str = str + getValue().toString();

        for (Iterator iterator = childNodes.iterator(); iterator.hasNext();) {
            str = str + "\n";
            TreeNode treeNode = (TreeNode) iterator.next();
            str = str + treeNode.toString();
        }

        return str;
    }
    
    /**
     * The method returns a clone of this tree node with the parent 
     * of the cloned node set to null.
     */
    public Object clone() {
    	TreeNode clone = new TreeNode();
    	clone.setDepth(this.depth);
    	clone.setPostion(this.position);
    	if(this.getValue() != null && this.getValue() instanceof AggregateExecutionTime)
    		clone.setValue( ((AggregateExecutionTime) this.getValue()).clone());
    	else  //  The else clause is to accommodate for the dummy node which is a String.
    		clone.setValue(this.getValue());
    	    	
    	List children = this.getChildren();
    	if(children != null && children.size() > 0) {
    		for(Iterator itr = children.iterator();itr.hasNext();){
    			TreeNode child = (TreeNode) itr.next();
    			clone.addChild((TreeNode) child.clone());
    		}
    	}    
    	return clone;
    }
    

    public void mergeAsChild(TreeNode node, Merger merger) {
        TreeNode nodeToMergeOnto = null;        
        for (Iterator i = this.childNodes.iterator(); i.hasNext();) {  
            TreeNode childNode = (TreeNode) i.next();
            if (merger.isMatching(childNode, node)) {
                nodeToMergeOnto = childNode;
                break;
            }
        }        
        if (nodeToMergeOnto != null) {
            merger.mergeValue(nodeToMergeOnto, node);
        } else {
            nodeToMergeOnto = merger.createNewNode(node);
            this.addChild(nodeToMergeOnto);
        }
        
        // @TODO: Clarity on what needs to be copied and what needs to be shared
        // Right now TreeNode instances are copied and the values are shared
        for (Iterator i = node.childNodes.iterator(); i.hasNext(); ) {
            TreeNode child = (TreeNode) i.next();
            nodeToMergeOnto.mergeAsChild(child, merger);
        }
    }
    
    // for the tests
    void setValue(Object value) {
        this.value = value;
    }
}