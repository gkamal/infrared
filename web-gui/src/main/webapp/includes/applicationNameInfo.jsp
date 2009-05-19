<!--
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
-->

<%@ page import="net.sf.infrared.web.util.PerformanceDataSnapshot,
                 java.text.SimpleDateFormat,
                 java.util.Date,
                 java.util.Set,
                 java.util.Iterator"%>

<%
    PerformanceDataSnapshot currentPerfData = (PerformanceDataSnapshot)session.getAttribute("perfData");
    Set selectedApplications = currentPerfData.getApplicationNames();
    Set selectedInstances = currentPerfData.getInstanceNames();
//    long startTime = currentPerfData.getStartTime();
//    long endTime = currentPerfData.getEndTime();
//    String startDate  = SimpleDateFormat.getTimeInstance().format(new Date(startTime));
//    String endDate  = SimpleDateFormat.getTimeInstance().format(new Date(endTime));
%>

<tr id="appNameCollapseRow">
	<td class="pageTitle" align="left" valign="middle">
    <img id="appNameImg" src="fw/def/image/tree/15/16.gif" width="13" height="13" onClick="showHide('appNameRow','appNameCollapseRow')" />
    Click to see currently selected Application
    </td>
</tr>

<tr id="appNameRow">
  <td class="pageTitle" valign="center">
    <img id="appNameImg" src="fw/def/image/tree/15/16.gif" width="13" height="13" onClick="showHide('appNameRow','appNameCollapseRow')"/>
	Application(s):&nbsp;
    <select MULTIPLE id="appInfo">
        <%
            for(Iterator itr=selectedApplications.iterator();itr.hasNext();)
            {
        %>
        <option> <%= itr.next()%> </option>
        <%
            }
        %>
    </select>
    &nbsp;&nbsp;&nbsp;
    Instance(s):&nbsp;
    <select MULTIPLE id="instInfo">
        <%
            for(Iterator itr=selectedInstances.iterator();itr.hasNext();)
            {
        %>
        <option> <%= itr.next()%> </option>
        <%
            }
        %>
     </select>
	&nbsp;&nbsp;&nbsp;
  </td>
</tr>
<script>
document.getElementById("appInfo").disabled=true;
document.getElementById("instInfo").disabled=true;
document.getElementById("appInfo").size=document.getElementById("appInfo").length;
document.getElementById("instInfo").size=document.getElementById("instInfo").length;
document.getElementById("appNameCollapseRow").style.display='none';
</script>

</body>
</html>