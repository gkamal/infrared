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

import java.util.HashMap;

import net.sf.infrared.base.util.TreeNode;
import net.sf.jsptree.tree.TreeFactory;
import net.sf.jsptree.tree.TreeFactoryException;
import net.sf.jsptree.tree.TreeStateModel;
import net.sf.jsptree.tree.TreeStructureModel;

/**
 * This class is responsible for creating the TreeState and TreeModel. A TreeFactoryImpl is created for
 * every api that is the head of a tree. This ensures that a seperate TreeModel exists for every api.
 * It consists of HashMaps that have references to the TreeState and TreeModel that correspond to this tree
 */

public class TreeFactoryImpl implements TreeFactory {
	public IRTreeStructureModel structureModel;

	public IRTreeStateModel stateModel;

	public NodeToStringConverter nodeToStringConverter;

	private HashMap treeStructureMap = null;

	private HashMap treeStateMap = null;

	public TreeFactoryImpl(TreeNode node, String treeName,
								NodeToStringConverter nodeToStringConverter) {
		treeStructureMap = new HashMap();
		treeStateMap = new HashMap();
		this.nodeToStringConverter = nodeToStringConverter;

		// This is required because we create a treefactory for every api.
		// If the call is from the JDBC layer, it will only contain SQL queries.
		// If an intermediate node is created in this case it returns the wrong data.
		// This method is similar to the getChildNodes in IRIntermediateNode

		if (node.getChildren().size() > 0)
			structureModel = new IRTreeStructureModel(new IRIntermediateNode(node, 
																			nodeToStringConverter));
		else {
			structureModel = new IRTreeStructureModel(new IRLeafNode(node,nodeToStringConverter));
		}
		stateModel = new IRTreeStateModel();
		treeStructureMap.put(treeName, structureModel);
		treeStateMap.put(treeName, stateModel);
	}

	public TreeStructureModel getTree(String treeName) throws TreeFactoryException {
		return (TreeStructureModel) treeStructureMap.get(treeName);
	}

	public TreeStateModel getTreeStates(String treeName) throws TreeFactoryException {
		return (TreeStateModel) treeStateMap.get(treeName);
	}

}
