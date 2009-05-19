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
 * Original Author:  G.Schulz (SCC)    Erstversion
 * Contributor(s):   sreejith.unnikrishnan (Tavant Technologies);
 *					
 *
 */

/////////////////////////////////////////////////////////////
// Global Variables used by the Framework
/////////////////////////////////////////////////////////////

var LF = '\n';
var NBSP = '';

var RunAt = new Object();
RunAt.SERVER = 0;
RunAt.CLIENT = 1;


/////////////////////////////////////////////////////////////
// Utility Classes
/////////////////////////////////////////////////////////////

/*
+ ---------------------------------------------------------------------------------+
| Purpose..:  Sets the color in a row of the Listcontrol if the cursors is moved
|             over the row.
|
| Date        Author            Notice
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/

function CCUtility() {}
function CCUtility_getEnclosingForm(node) {
	// search the form wich embbeds the Element
	var parent = node.parentNode;
	
	if (null == parent) return null;
	
	if (parent.nodeName == 'FORM' ) {
		return parent;
	} else {
		return arguments.callee(parent);
	}
}
function CCUtility_createHidden(fldName, fldValue) {
	var input=document.createElement('INPUT');
	input.type='hidden';
	input.id=fldName;
	input.name=fldName;
	input.value=fldValue;
	return input;
}
function CCUtility_crtCtrla(node, param) {
	var form = this.getEnclosingForm(node);
	
	if (null == form) {
		// form not specified -> do nothing
		return;
	} else {
		form.appendChild(this.createHidden('ctrla', param));
		form.submit();

	}
}

new CCUtility();
CCUtility.getEnclosingForm   = CCUtility_getEnclosingForm;
CCUtility.createHidden       = CCUtility_createHidden;
CCUtility.crtCtrla           = CCUtility_crtCtrla;

