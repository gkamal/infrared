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

<%@ page import="java.util.StringTokenizer"
         contentType="text/html; charset=iso-8859-1" language="java" errorPage=""
%>
<html><!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<script language="JavaScript" src="common/javascripts/retail_LOS.js" type="text/javascript"></script>
<link href="common/stylesheets/master.css" rel="stylesheet" type="text/css">
</head>

<body>
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
        <%
            request.setAttribute("highlight","help");
        %>
        <jsp:include page="includes/tabDetails.jsp"/>
      <!-- InstanceEndEditable --></td>
  </tr>
  <tr>
    <td class="noBorderCell"> <!-- InstanceBeginEditable name="secondNav" -->
      <!-- InstanceEndEditable --></td>
  </tr>
  <tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="Error" -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="error" style="display:none">Error</td>
        </tr>
      </table>
      <!-- InstanceEndEditable --></td>
  </tr>
  <tr>
  <tr>
    <td class="pageTitle"><!-- InstanceBeginEditable name="pageTitle" -->Help<!-- InstanceEndEditable --></td>
  </tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
      <!-- InstanceEndEditable --></td>
  </tr>
</table>
</body>
<!-- InstanceEnd --></html>
