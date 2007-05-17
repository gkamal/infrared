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
 * Original Author:  binil.thomas (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.base.util;

import java.io.Serializable;

/**
 * Interface that defines the operations during merging two trees.
 * 
 * @author binil.thomas
 */
public interface Merger extends Serializable {
    /**
     * Merge value held by one TreeNode (toMerge) onto value held by another TreeNode (mergeOnto).
     */
    void mergeValue(TreeNode mergeOnto, TreeNode toMerge);
        
    /**
     * Create a new TreeNode representing the same value as the given TreeNode
     * @param from
     * @return
     */
    TreeNode createNewNode(TreeNode from);
        
    /**
     * Checks if values held by two TreeNode objects are merge-able.
     */
    boolean isMatching(TreeNode original, TreeNode toMerge);
}
