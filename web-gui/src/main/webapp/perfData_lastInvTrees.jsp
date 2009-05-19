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

<%@ page pageEncoding="ISO-8859-1"
	import="net.sf.infrared.web.util.ViewUtil,net.sf.infrared.web.util.SqlStatistics,net.sf.infrared.base.util.TreeNode,net.sf.infrared.base.model.AggregateOperationTree,net.sf.infrared.web.util.PerformanceDataSnapshot,java.util.StringTokenizer,net.sf.infrared.web.treecontrolmodel.TreeFactoryImpl,net.sf.jsptree.skin.AbstractSkin,java.util.ArrayList"
	contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>

<%@ taglib uri="struts/logic" prefix="slogic"%>
<%@ taglib uri="struts/bean" prefix="sbean"%>
<%@ taglib uri="jsptree/jsptree" prefix="sf"%>
<%@ taglib uri="jstl/c" prefix="c"%>

<%int treeNumber = 0;
			ArrayList lastInvocationBeans = (ArrayList) session
					.getAttribute("lastInvBeanTree");
			String[] expandMode = null;
			String[] expandIcon = null;
			String[] expandIconHref = null;
			Integer length = new Integer(lastInvocationBeans.size());
			pageContext.setAttribute("lastInvLength", length);

			PerformanceDataSnapshot perfData = null;
			String sqlTableName = null;
			String collapsedRowName = null;
			int colSpan = 4;
			boolean fetchEnabled = true;

			if (lastInvocationBeans.size() > 0) {
				expandMode = new String[lastInvocationBeans.size()];
				expandIcon = new String[lastInvocationBeans.size()];
				expandIconHref = new String[lastInvocationBeans.size()];
				for (int i = 0; i < lastInvocationBeans.size(); i++) {
					expandMode[i] = "multiple";
					expandIcon[i] = "fw/def/image/tree/Expand.gif";
					expandIconHref[i] = "perfData_lastInvTrees.jsp?expandMode=tree@"
							+ i + "@full";
				}
				String requestedExpandMode = request.getParameter("expandMode");
				if (requestedExpandMode != null) {
					StringTokenizer strTok = new StringTokenizer(
							requestedExpandMode, "@");
					strTok.nextToken();
					treeNumber = Integer.parseInt(strTok.nextToken());
					expandMode[treeNumber] = strTok.nextToken();
					if ("full".equals(expandMode[treeNumber])) {
						expandIcon[treeNumber] = "fw/def/image/tree/Contract.gif";
						expandIconHref[treeNumber] = "perfData_lastInvTrees.jsp?expandMode=tree@"
								+ treeNumber + "@multiple";
					} else {
						expandIcon[treeNumber] = "fw/def/image/tree/Expand.gif";
						expandIconHref[treeNumber] = "perfData_lastInvTrees.jsp?expandMode=tree@"
								+ treeNumber + "@full";
					}
				}
			}
%>
<jsp:useBean id="isInActiveMode" scope="session"
	class="java.lang.String" />

<html>
<!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script src="fw/global/jscript/common.js" language="JavaScript"> </script>
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
</script>
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet"
	type="text/css">
</head>

<body>
<form action="<%=response.encodeURL("perfData_lastInvTrees.do")%>"
	method="GET">

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
		<%request.setAttribute("highlight", "lastInv");

			%> <jsp:include page="includes/tabDetails.jsp" /> <!-- InstanceEndEditable --></td>
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
		<slogic:notEqual name="isInActiveMode" value="false">
			<td class="pageTitle">The application is in active mode</td>
		</slogic:notEqual>

		<slogic:equal name="isInActiveMode" value="false">
			<%String fileName = (String) session.getAttribute("fileName");

			%>
			<td class="pageTitle">The snapshot loaded is <%=fileName%></td>
		</slogic:equal>
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

						<slogic:notEqual name="isInActiveMode" value="false">
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do?reset=true")%>">Reset</a></td>
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do")%>">Refresh</a></td>
						</slogic:notEqual>
						<slogic:equal name="isInActiveMode" value="false">
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do?activeMode=true")%>">Go
							Back to Active Mode</a></td>
						</slogic:equal>
					</tr>
				</table>
				</td>
			</tr>

			<%if (lastInvocationBeans.size() > 0) {

				%>

			<%String imagePath = request.getContextPath()
						+ "/imgs/tree/menu/blue/";
				int i = -1;

				%>

			<c:forEach items="${sessionScope.lastInvBeanTree}"
				var="invocationTrees">
				<%i++;
				String callTraceTable = "callTrace" + i;
				String extraLine = "extraLine" + i;
				TreeFactoryImpl treeFactory = (TreeFactoryImpl) session
						.getAttribute("InfraTree" + i);
				pageContext.setAttribute("treeFactory", treeFactory);
				String treeName = "InfraTree" + i;

				%>
				<tr id="<%=extraLine%>">
					<td class="lablel1Bold" align="left" valign="middle"><img
						src="fw/def/image/tree/15/32.gif"
						onClick="showHide('<%=callTraceTable%>','<%=extraLine%>')" /> Call
					Tree Sequence <%=(i + 1)%></td>
				</tr>
				<script>
            document.getElementById('<%=extraLine%>').style.display='none';
        </script>


				<tr id="<%=callTraceTable%>">
					<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold"><img src="fw/def/image/tree/15/16.gif"
								onClick="showHide('<%=callTraceTable%>','<%=extraLine%>')" />
							Call Tree Sequence <%=(i + 1)%></td>
						</tr>
						<tr>
							<td colspan="3"><a
								href="<%=response.encodeURL(expandIconHref[i])%>"> <img
								src="<%=expandIcon[i]%>" width="10" height="10" border="0" /></a>
							<sf:JSPTree name="<%=treeName%>" treeFactory="treeFactory"
								startAtDepth="0" imagesPath="<%=imagePath%>"
								templatePath="net/sf/jsptree/example/template/menu/"
								shareTreeStructure="false" expandMode="<%=expandMode[i]%>"
								skin="<%=AbstractSkin.MENU_SKIN%>" />
						   </td>
						</tr>
						<slogic:notEqual name="invocationTrees"
							property="jdbcSummaryLength" value="0">
							<tr>
								<td class="lablel1Bold" colspan="3">JDBC Summary</td>
							</tr>
							<tr>
								<td class="noBorderCell" width="33%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="100%" nowrap class="lablel1Bold">API Name</td>
									</tr>
								</table>
								</td>
								<td width="33%" class="noBorderCell">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="100%" nowrap class="lablel1Bold">Average Time (ms)</td>
									</tr>
								</table>
								</td>
								<td width="33%" class="noBorderCell">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="100%" nowrap class="lablel1Bold">Count</td>
									</tr>
								</table>
								</td>
							</tr>

							<slogic:iterate id="aggregateApiTime" name="invocationTrees"
								property="aggregateApiBean">
								<tr>
									<td><sbean:write name="aggregateApiTime" property="apiName" /></td>
									<td><sbean:write name="aggregateApiTime"
										property="averageInclusiveTime" /></td>
									<td><sbean:write name="aggregateApiTime"
										property="executionCount" /></td>
								</tr>

							</slogic:iterate>
						</slogic:notEqual>
						<tr>
							<td>&nbsp;</td>
						</tr>

						<slogic:greaterThan name="invocationTrees"
							property="sqlStatsLength" value="0">

							<tr>
								<td class="lablel1Bold">SQL queries sorted by Average Execution
								Time</td>
							</tr>
							<%String SqlTableName = "sortedByAvgExecTime" + i;
				String collapseRowName = "sortedByAvgExecTimeCollapsedRow" + i;
				request.setAttribute("sqlTableName", SqlTableName);
				request.setAttribute("collapsedRowName", collapseRowName);
				ArrayList sqlStatsList = (ArrayList) session.getAttribute("sqlStats");
				request.setAttribute("sqlStatistics", sqlStatsList.get(i));

				%>

							<%@ include file="includes/sqlStatistics.jsp"%>

						</slogic:greaterThan>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
			</c:forEach>
			<%} else {

			%>


			<!--logic:equal name="lastInvLength" value="0"-->
			<p align="center">Last invocation Call Sequence not available</p>
			<%}

		%>
			<!--/logic:equal-->

		</table>
		<form>
</body>
<!-- InstanceEnd -->
</html>
