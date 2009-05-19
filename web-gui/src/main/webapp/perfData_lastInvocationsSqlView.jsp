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

<%@ page
	import="net.sf.infrared.web.ui.ViewUtil,net.sf.infrared.base.model.AggregateOperationTree,net.sf.infrared.collector.PerformanceDataSnapShot"
	contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>

<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>

<%
    PerformanceDataSnapShot perfData = (PerformanceDataSnapShot)session.getAttribute("perfData");
%>
<jsp:useBean id="isInActiveMode" scope="session" type="java.lang.String" />

<html>
<!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script src="fw/global/jscript/common.js" language="JavaScript"> </script>
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet"
	type="text/css">
<script>
	function showHide(node1,node2)
	{
		if(document.getElementById(node1).style.display=='none')
		{
			document.getElementById(node1).style.display='block';
            document.getElementById(node2).style.display='none';
		}
		else
		{
			document.getElementById(node1).style.display='none';
            document.getElementById(node2).style.display='block';
		}
	}
	function showHideSqlView(node1,node2)
	{
		if(document.getElementById(node1).style.display=='none')
		{
			document.getElementById(node1).style.display='block';
            document.getElementById(node2).src='fw/def/image/tree/15/16.gif';
		}
		else
		{
			document.getElementById(node1).style.display='none';
            document.getElementById(node2).src='fw/def/image/tree/15/32.gif';
		}
	}
</script>
</head>

<body>
<table width="780" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="noBorderCell">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="50%" class="tavantLogo">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="Navigation1" -->
		<%
            request.setAttribute("highlight","lastInv");
        %> <jsp:include page="includes/tabDetails.jsp" /> <!-- InstanceEndEditable --></td>
	</tr>
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="secondNav" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" nowrap class="subMenuUp">&nbsp;</td>
				<td nowrap class="menuUp"><a
					href="<%=response.encodeURL("perfData_use_jspTree.do?pageType=lastinvocations")%>"
					class="menu">Call sequence view</a></td>
				<td nowrap class="subMenuDown">Sql Query view</td>
			</tr>
		</table>
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
	<jsp:include page="includes/applicationNameInfo.jsp" />
	<tr>
	<tr>
		<logic:notEqual name="isInActiveMode" value="false">
			<td class="pageTitle">The application is in active mode</td>
		</logic:notEqual>

		<logic:equal name="isInActiveMode" value="false">
			<%
            String fileName = (String)session.getAttribute("fileName");
        %>
			<td class="pageTitle">The snapshot loaded is <%=fileName%></td>
		</logic:equal>

	</tr>
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="lablel1Bold">Call Sequences of the Last invocations
						(latest first)</td>
						<logic:equal name="isInActiveMode" value="true">
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_lastInvocationsSQLView.do?reset=true")%>">Reset</a></td>
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_lastInvocationsSQLView.do?refresh=true")%>">Refresh</a></td>
						</logic:equal>
						<logic:notEqual name="isInActiveMode" value="true">
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do?activeMode=true")%>">Go
							Back to Active Mode</a></td>
						</logic:notEqual>
					</tr>
				</table>
				</td>
			</tr>
			<jsp:useBean id="lastInvTreeLength" scope="request"
				type="java.lang.Integer" />
			<logic:equal name="lastInvTreeLength" value="0">
				<p align="center">Last invocation Call Sequence not available</p>
			</logic:equal>

			<logic:notEqual name="lastInvTreeLength" value="0">
				<%
            int i = 0;
        %>
				<logic:iterate id="lastInvTree" collection="<%=request.getAttribute(\"lastInvocationTrees\")%>">
			
					<%
                    String sqlImg = "sqlImg"+i;
                    String sqlTable = "sqlTable"+i;
        %>
					<tr>
						<td class="lablel1Bold"><img src="fw/def/image/tree/15/16.gif"
							id="<%=sqlImg%>"
							onClick="showHideSqlView('<%=sqlTable%>','<%=sqlImg%>')" /> Call
						Tree Sequence <%=(i + 1)%></td>
					</tr>
					<tr id="<%=sqlTable%>">
						<td class="noBorderCell">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<logic:greaterThan name="lastInvTree" property="noOfSqlCalls"
								value="0">

								<tr>
									<td colspan="3" class="noBorderCell"><%=ViewUtil.getSqlLog(perfData,(AggregateOperationTree)lastInvTree)%>
									</td>
								</tr>
							</logic:greaterThan>
							<logic:equal name="lastInvTree" property="noOfSqlCalls" value="0">
								<tr>
									<td class="noBorderCell">This call sequence doesn't have any
									SQL calls</td>
								</tr>
							</logic:equal>
						</table>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<%
            i++;
        %>
				</logic:iterate>
			</logic:notEqual>
		</table>
		<!-- InstanceEndEditable --></td>
	</tr>
</table>
</body>
<!-- InstanceEnd -->
</html>

