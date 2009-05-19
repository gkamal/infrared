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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.treecontrolmodel;

import java.util.ArrayList;
import java.util.List;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.util.TreeNode;

/**
 *  Represents a leaf level node in the tree.
 */
public class IRLeafNode implements net.sf.jsptree.tree.TreeNode {
	public AggrApiJspTreeNode treeNode;

	public TreeNode node;

	public NodeToStringConverter nodeToStringConverter;

	public IRLeafNode(TreeNode node, NodeToStringConverter nodeToStringConverter) {
		this.node = node;
		AggregateExecutionTime aggApiTime = (AggregateExecutionTime) node.getValue();
		this.treeNode = new AggrApiJspTreeNode(aggApiTime, nodeToStringConverter);
		this.nodeToStringConverter = nodeToStringConverter;
	}

	public Object getData() {
		return treeNode;
	}

	public List getChildNodes() {
		return new ArrayList();
	}

	public int getDepth() {
		return node.getDepth();
	}

	public boolean hasChildren() {
		return false;
	}

	public int getChildCount() {
		return 0;
	}

	public Object clone() throws CloneNotSupportedException {
		return null;
	}

}
