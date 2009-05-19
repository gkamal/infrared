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

public class IRIntermediateNode implements net.sf.jsptree.tree.TreeNode {
	public TreeNode node;

	public NodeToStringConverter nodeToStringConverter;

	private AggrApiJspTreeNode treeNode;

	private List children;

	public IRIntermediateNode(TreeNode node, NodeToStringConverter nodeToStringConverter) {
		this.node = node;
		this.nodeToStringConverter = nodeToStringConverter;
		treeNode = new AggrApiJspTreeNode((AggregateExecutionTime) node.getValue()
												,nodeToStringConverter);
		children = new ArrayList();
	}

	public Object getData() {
		return treeNode;
	}

	public List getChildNodes() {
		List nodeList = node.getChildren();
		// We do not want to rebuild the tree again for the same node.
		if (children.size() == 0) {
			for (int i = 0; i < nodeList.size(); i++) {
				net.sf.infrared.base.util.TreeNode childNode = 
									(net.sf.infrared.base.util.TreeNode) nodeList.get(i);
				if (childNode.getChildren().size() == 0) {
					AggregateExecutionTime aggApiTime = (AggregateExecutionTime) childNode
																				.getValue();
						children.add(new IRLeafNode(childNode,nodeToStringConverter));
				} 
				else {
					children.add(new IRIntermediateNode(childNode,nodeToStringConverter));
				}
			}
		}
		return children;
	}

	public int getDepth() {
		return node.getDepth();
	}

	public boolean hasChildren() {
		if (node.getChildren().size() > 0)
			return true;
		else
			return false;
	}

	public int getChildCount() {
		return node.getChildren().size();
	}

	public Object clone() throws CloneNotSupportedException {
		return (IRIntermediateNode) super.clone();
	}

	public int hashCode() {
		return treeNode.hashCode();
	}

}
