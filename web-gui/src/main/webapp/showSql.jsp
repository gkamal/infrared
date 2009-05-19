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

<%@ page import="net.sf.infrared.web.util.PerformanceDataSnapshot,
 				net.sf.infrared.web.util.sql.SQLToHtml"
         contentType="text/html; charset=iso-8859-1" language="java" errorPage=""
%>
<html><!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>SQL Query</title>
<link href="common/stylesheets/master.css" rel="stylesheet" type="text/css">
<%-- 
	TODO: This css entry had been moved to external file syntax.css. But somehow the css was not 
	reflected back in the rendered html page. So as a quick fix I moved it here.
--%>
<style>
TD {
	font-family:  Arial, Verdana,Helvetica, sans-serif;
	font-size: 12px;
	line-height: 15px;
	border: 1px solid #EFEFEF;
	vertical-align: top;
	padding: 2px;
	white-space: nowrap;
	background-color: #FFFFFF;
}
body {
	background:white;
}
.tag{
	font-weight:bold;
	color:blue;
}
.endtag{
	color:blue;	
}
.reference{
	color:black;
}
.name{
	font-weight:bold;
	color:maroon;
}
.value{
	font-style: italic;
	color:maroon;
}
.text{
	font-weight:bold;
	color:black;
}
.reservedWord{
	font-weight:bold;
	color:brown;
}
.identifier{
	color:black;
}
.literal{
	color:blue;
}
.quoted{
	color:green;
}
.separator{
	color:navy;
}
.operator{
    font-weight:bold;
    color:navy;
}
.comment{
	color:green;
}
.preprocessor{
	color:purple;
}
.whitespace{
}
.error{
	color:red;
}
.unknown{
	color:orange;
}
</style>
</head>

<%
    String sourceSql = request.getParameter("name");
    String sql = SQLToHtml.convertToHtml(sourceSql);
%>
<body>
<table width="100%">
<tr>
<td>
<%=sql%>
</td>
</tr>
</table>
</body>
</html>