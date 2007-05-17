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
 * Original Author:  binil.thoms (Tavant Technologies)
 * Contributor(s):   prashant.nair, subin.p
 *
 */
package net.sf.infrared.base.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.base.util.Merger;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

import org.apache.log4j.Logger;

/**
 * Represents the aggregate of a set of operations in tree form.
 * 
 * <p>
 * This holds a Tree which has a dummy node as the root. The children of this
 * dummy node are TreeNodes representing AggregateExecutionTimes.
 * 
 * <p>
 * Initially an AggregateOperationTree starts out empty, and more data is added
 * to it by calls to the the merge(Tree) and merge(AggregateOperationTree)
 * methods.
 * 
 * @author binil.thomas
 * @author prashant.nair
 * @author subin.p
 */
public class AggregateOperationTree implements Serializable {

    private static final Logger log = LoggingFactory.getLogger(AggregateOperationTree.class);

    private Tree aggregateTree;
    
    private Merger opMerger = new OperationTreeMerger();
    
    private Merger aggMerger = new AggregateOperationTreeMerger();

    /**
     * Creates an empty AggregateOperationTree
     */
    public AggregateOperationTree() {
        aggregateTree = new Tree();
        aggregateTree.setRoot(TreeNode.createTreeNode("dummy root"));
    }
    
    /**
     * Merges an execution tree onto this AggregateOperationTree.
     * 
     * @param anOperation the tree of ExecutionTimer objects, which represents the
     *                    executions in one thread
     */
    public synchronized void merge(Tree anOperation) {
        aggregateTree.getRoot().mergeAsChild(anOperation.getRoot(),  opMerger);
        if (log.isDebugEnabled()) {
            log.debug("Merged operation tree with root " + anOperation.getRoot().getValue());
        }
    }

    /**
     * Merges another AggregateOperationTree onto this AggregateOperationTree. The
     * passed in AggregateOperationTree is left unchanged.
     * 
     * @param anOperation the tree of ExecutionTimer objects, which represents the
     *                    executions in one thread
     */
    public synchronized void merge(AggregateOperationTree otherTree) {
        // The root of an AggregateOperationTree is always a dummy node (this is to accomodate the
        // fact that there could be multiple entry points in an application).
        // Therefore, we can always safely ignore the dummy node and just merge the children
        List othersChildren = otherTree.getAggregateTree().getRoot().getChildren();
        TreeNode rootOfThis = getAggregateTree().getRoot(); // this is the dummy node we have
        for (Iterator iter = othersChildren.iterator(); iter.hasNext();) {
            TreeNode othersChild = (TreeNode) iter.next();
            rootOfThis.mergeAsChild(othersChild, aggMerger);
        }
        if (log.isDebugEnabled()) {
            log.debug(this + " - Merged AggregateOperationTree " + otherTree);
        }
    }
    
    public String toString(){
        return aggregateTree.getRoot().toString();
    }
        
    public Tree getAggregateTree() {
        return aggregateTree;
    }

    public void setAggregateTree(Tree aggregateTree) {
        this.aggregateTree = aggregateTree;
    }    
}

abstract class TimeMerger implements Merger {
    protected ExecutionTimer getExecutionTimer(TreeNode nodeWithATimer) {
        return (ExecutionTimer) nodeWithATimer.getValue();
    }
    
    protected AggregateExecutionTime getAggregateExecutionTime(TreeNode nodeWithAnAggregate) {
        return (AggregateExecutionTime) nodeWithAnAggregate.getValue();
    }
}

class OperationTreeMerger extends TimeMerger {
    public TreeNode createNewNode(TreeNode from) {
        ExecutionTimer et = getExecutionTimer(from);
        AggregateExecutionTime aet = new AggregateExecutionTime(et.getContext());
        aet.merge(et);
        return TreeNode.createTreeNode(aet);
    }

    public void mergeValue(TreeNode mergeOnto, TreeNode toMerge) {
        AggregateExecutionTime aet = getAggregateExecutionTime(mergeOnto);
        ExecutionTimer et = getExecutionTimer(toMerge);
        aet.merge(et);
    }

    public boolean isMatching(TreeNode original, TreeNode toMerge) {
        AggregateExecutionTime aet = getAggregateExecutionTime(original);
        ExecutionTimer et = getExecutionTimer(toMerge);
        
        return aet.getContext().equals( et.getContext() );
    }         
}

class AggregateOperationTreeMerger extends TimeMerger {
    public TreeNode createNewNode(TreeNode from) {
        AggregateExecutionTime aet = getAggregateExecutionTime(from);    
        return TreeNode.createTreeNode(aet);
    }

    public void mergeValue(TreeNode mergeOnto, TreeNode toMerge) {
        AggregateExecutionTime aet1 = getAggregateExecutionTime(mergeOnto);
        AggregateExecutionTime aet2 = getAggregateExecutionTime(toMerge);
        aet1.merge(aet2);
    }             

    public boolean isMatching(TreeNode original, TreeNode toMerge) {
        AggregateExecutionTime aet1 = getAggregateExecutionTime(original);
        AggregateExecutionTime aet2 = getAggregateExecutionTime(toMerge);
        
        return aet1.getContext().equals( aet2.getContext() );
    }
}
