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
 * Original Author:  kaushal.kumar (Tavant Technologies)
 * Contributor(s):   prashant.nair;
 *
-->

<%
    String fileName = (String)request.getAttribute("fileName");
    String errorType = (String)request.getAttribute("errorType");
    String message="";
    if(errorType.equals("file-overwrite-error"))
    {
        message = "<B>This SnapShot file already exists.<br><br>Overwrite ??</B><br>";
    }
    if(errorType.equals("outputstream-error"))
    {
        message = "<br><br><B> There was an error in persisting the data into file</B><br><br>";
    }
    if(errorType.equals("inputstream-error"))
    {
        message = "<br><br><B> Please select a Snapshot that needs to be loaded. </B><br><br>";
    }

%>
<html><!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet" type="text/css">
</head>

<body>
<!-- %
    if(monitoringLabel==null)
    {
%-->
        <!--jsp:forward page="configureAction.do"/-->
<!-- %
    }
% -->



<table width="780" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="50%" class="tavantLogo">&nbsp;</td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="Navigation1" -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="100%" nowrap class="menuUp">&nbsp;</td>
          <td nowrap class="menuUp"><a href="<%=response.encodeURL("perfData_summaryAction.do")%>" class="menu">Performance Summary</a></td>
          <td nowrap class="menuUp"><a href="<%=response.encodeURL("perfData_lastInvocationTrees.do")%>" class="menu">Last Invocations</a></td>
          <td nowrap class="menuUp"><a href="<%=response.encodeURL("httpResponseSummaryAction.do")%>" class="menu">HTTP Response</a></td>
          <%
              if("no".equals(session.getAttribute("isInCentralMode")))
              {
          %>
          <td nowrap class="menuUp"><a href="<%=response.encodeURL("configureAction.do")%>" Class="menu">Configure</a></td>
          <%
              }
          %>
          <td nowrap class="menuUp"><a href="<%=response.encodeURL("handleSnapshotAction.do")%>" class="menu">Snapshots</a></td>
          <td nowrap class="menuUp"><a href="<%=response.encodeURL("help.jsp")%>" class="menu">Help</a></td>
        </tr>
      </table>
      <!-- InstanceEndEditable --></td>
  </tr>

  <tr></tr>
  <tr></tr>

  <%
      if(errorType.equals("file-overwrite-error"))
      {
  %>
  <form ACTION="handleSnapshotAction.do?action=save&fileName=<%=fileName%>" METHOD="POST">
  <%
      }
  %>
  <tr>
    <td class="noBorderCell"><table width="100%" border="2" cellspacing="0" cellpadding="0">
    <tr>
        <td align="center" border="2">
        <%=message%>
  <%
      if(errorType.equals("file-overwrite-error"))
      {
  %>
            <input type=submit name="overwrite" value="yes"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type=submit name="overwrite" value="no"/>
  <%
      }
  %>
        </td>
   </tr>
   </table>
   </td>
  </tr>
  </form>

  <tr>
    <td class="noBorderCell"><jsp:include page="includes/footer.jsp" flush="true" /></td>
  </tr>
</table>
</body>
<!-- InstanceEnd --></html>
