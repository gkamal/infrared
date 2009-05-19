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
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.AggregateOperationTree;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.base.util.TreeNode;

import org.apache.log4j.Logger;

public class TreeUtil {

    private static Logger log = LoggingFactory.getLogger("net.sf.infrared.web.util.TreeUtil");

    private static ExecutionContext context;


    /**
     * This method creates an ExecutionContext based on the ctxName, the actualLayer
     * and the ctxClassName using reflection. Further, this method merges all the
     * AggregateExecutionTimes in the given AggregateOperationTree if the ExecutionContexts
     * match. If the hierarchicalLayer parameter is a not null value, only those
     * AggregateExecutionTimes in the tree whose hierarchical layer mathces the
     * given hierarchicalLayer parameter shall be merged.
     *
     * @param rootOperTree
     * @param ctxName
     * @param layer
     * @return
     */
    public static TreeNode getMergedExecutionContextTreeNode(AggregateOperationTree rootOperTree,
            String ctxName, String actualLayer, String ctxClassName, String hierarchicalLayer) {

        if(ctxName == null)
            throw new IllegalArgumentException("The ctxName argument for" +
                    "the method getMergedApiContextTreeNode is null");

        context = getExecutionContext(ctxName, actualLayer, ctxClassName);

        // Cloning the rootOperTree before merging the same.
        AggregateOperationTree cloneTree = new AggregateOperationTree();
        TreeNode cloneNode = (TreeNode) rootOperTree.getAggregateTree().getRoot().clone();
        cloneTree.getAggregateTree().setRoot(cloneNode);

        return getMergedExecutionContextTreeNode(cloneTree, context, hierarchicalLayer);
    }



	static ExecutionContext getExecutionContext(String apiString, String layer, String ctx) {
        ExecutionContext newExecutionContext = null;
		try {
            newExecutionContext = (ExecutionContext)Class.forName(ctx)
							 .getConstructor(new Class[]{apiString.getClass(), layer.getClass()})
							 .newInstance(new Object[]{apiString, layer});
		} catch (IllegalArgumentException e) {
			log.error("The arguments passed to the constructor are incorrect" , e);
            return null;
		} catch (SecurityException e) {
			log.error("Security Exception " , e);
            return null;
		} catch (InstantiationException e) {
			log.error("Instantiation Exception ", e);
            return null;
		} catch (IllegalAccessException e) {
			log.error("Illegal Argument Exception ", e);
            return null;
		} catch (InvocationTargetException e) {
			log.error("Invocation Target Exception ", e);
            return null;
		} catch (NoSuchMethodException e) {
			log.error(e);
            return null;
		} catch (ClassNotFoundException e) {
			log.error(e);
            return null;
		}
        return newExecutionContext;
	}


    /**
     * This is the general utility method to merge all the statistics corresponding to an
     * ExecutionContext in a given AggregateOperationTree.
     * @param rootOperTree
     * @param context
     * @return TreeNode
     */
    static TreeNode getMergedExecutionContextTreeNode(AggregateOperationTree rootOperTree,
            ExecutionContext context, String hierarchicalLayer) {
        if (rootOperTree == null || rootOperTree.getAggregateTree().getRoot() == null
                || context == null) {

            log.error("The rootOperTree or the ExecutionContext cannot be null" +
                    " for the getMergedExecutionContextTreeNode method");
            return null;
        }
        TreeNode rootNode = rootOperTree.getAggregateTree().getRoot();
        List subTrees = extractMatchingOperationTrees(rootNode,
        		context, hierarchicalLayer, new ArrayList());
        AggregateOperationTree mergedTree = new AggregateOperationTree();

        // Merging the subtrees.
        for(Iterator itr = subTrees.iterator();itr.hasNext();) {//comment this
        	AggregateOperationTree opTree = (AggregateOperationTree) itr.next();
        	mergedTree.merge(opTree);
        }

        // Need to return the merged node matching the context parameter & not the dummy node.
        List children = mergedTree.getAggregateTree().getRoot().getChildren();

        //@TODO do we need to check for call trace disabling early on ?

        if(children == null || children.size() < 1) // probably call tracing is disabled.
        	return null;

        TreeNode resultantNode = (TreeNode) children.get(0);// The dummy root should have only one child.
        resultantNode.setDepth(0);
        return resultantNode;
    }


    static List extractMatchingOperationTrees(TreeNode rootTreeNode,
    		ExecutionContext matchingContext, String hierarchicalLayer, List subTrees) {
    	Object rootValue = rootTreeNode.getValue();
    	if(rootValue instanceof AggregateExecutionTime && matchingContext.equals(
		  ((AggregateExecutionTime)rootValue).getContext()  ) ) {
			AggregateExecutionTime rootAggExecTime = (AggregateExecutionTime)rootValue;

			if(hierarchicalLayer == null || hierarchicalLayer.equals( rootAggExecTime.getLayerName()) ){
	            AggregateOperationTree matchingTree = new AggregateOperationTree();

	            // The subtree should have the dummy node as the root so that they can be merged using the
	            // merge api provided in the AggregateExecutionTime class.
	            rootTreeNode.setParent(null);
	            matchingTree.getAggregateTree().getRoot().addChild(rootTreeNode); //comment this
	            subTrees.add(matchingTree);
	            return subTrees;
			}
    	}
		List childList = rootTreeNode.getChildren();
        for(Iterator itr = childList.iterator();itr.hasNext();)
        {
            TreeNode currNode = (TreeNode) itr.next();
            extractMatchingOperationTrees(currNode,matchingContext,hierarchicalLayer,subTrees);
        }
        return subTrees;
    }


    static Map getContextList(List children) {
        Map returnMap = new HashMap();

        for (Iterator iter = children.iterator(); iter.hasNext();) {
            TreeNode element = (TreeNode) iter.next();
            AggregateExecutionTime execTime = (AggregateExecutionTime)element.getValue();
            returnMap.put(execTime.getContext(), execTime);
        }
        return returnMap;
    }
}
