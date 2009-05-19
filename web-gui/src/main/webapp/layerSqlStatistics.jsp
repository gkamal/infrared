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

<%@ page import="net.sf.infrared.web.formbean.SqlStatisticsFormBean"
	contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>

<%@ taglib uri="struts/logic" prefix="logic"%>
<%@ taglib uri="struts/bean" prefix="bean"%>
<%@ taglib uri="infrared/infrared" prefix="infrared"%>
<%
    String type = (String)request.getAttribute("apiType");
    int n = 5;
%>

<jsp:useBean id="isInActiveMode" scope="session" type="java.lang.String" />


<html>
<!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<SCRIPT SRC="../../javascript/functions.js"></SCRIPT>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet"
	type="text/css">
<script>
function showSql(sqlid)
{
  window.open('showSql.jsp?name=sqlname','SqlQuery','height=200,width=400,location=no,resizable=yes,scrollbars=yes,toolbars=no');
}
</script>	
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
	<jsp:useBean id="apiType" scope="request" type="java.lang.String" />
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="Navigation1" -->
		<%
            request.setAttribute("highlight","summary");
        %> <jsp:include page="includes/tabDetails.jsp" /> <!-- InstanceEndEditable --></td>
	</tr>
	<tr>
		<td class="noBorderCell"></td>
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
		<logic:notEqual name="isInActiveMode" value="false">
			<td class="pageTitle">The application is in active mode</td>
		</logic:notEqual>

		<logic:equal name="isInActiveMode" value="false">
			<td class="pageTitle">The data is loaded from the DB</td>
		</logic:equal>
	<tr>
		<td class="moduleTitle">Summary of module: <%=apiType%>
	</tr>
	<tr>
		<td class="noBorderCell">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="lablel1Bold"><a href="perfData_summaryAction.do">Go
						Back To Summary Of All Modules</a></td>
						<logic:equal name="isInActiveMode" value="true">

							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do?reset=true")%>">Reset</a></td>
							<td class="lablel1Bold" aling="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do")%>">Refresh</a></td>
						</logic:equal>

					</tr>
				</table>
				</td>
			</tr>
			<tr id="extraLine">
				<td class="lablel1Bold" align="left" valign="middle"><img
					src="fw/def/image/tree/15/32.gif"
					onClick="showHide('apiNameTable','extraLine')" /> Api Summary</td>
			</tr>
			<script>
            	document.getElementById('extraLine').style.display='none';
        	</script>

			<tr>
				<td class="lablel1Bold">Top <%=n%> SQL queries by Average Execution
				Time</td>
			</tr>

			<tr>
				<td class="noBorderCell">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="lablel1Bold" width="30%" rowspan="2">Sql Query</td>
						<td class="lablel1Bold" colspan="3">Avg. Time(ms)</td>
						<td class="lablel1Bold" colspan="2">Count</td>
						<td class="lablel1Bold" colspan="2">Max. Time(ms)</td>
						<td class="lablel1Bold" colspan="2">Min. Time(ms)</td>
						<td class="lablel1Bold" colspan="2">First Exec.Time(ms)</td>
						<td class="lablel1Bold" colspan="2">Last Exec.Time(ms)</td>
					</tr>
					<tr>
						<td class="lablel1Bold">Total</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
					</tr>
					<logic:iterate id="sqlStats"
						collection="<%= request.getAttribute(\"sqlStatisticsByExec\") %>">
						<tr>
							<td
								class="wrappedTD <bean:write name="sqlStats" property="tdStyle"/>"
								width="30%"><%
                            String sql = ((SqlStatisticsFormBean)sqlStats).getSql();
							String escapedSql =sql.replaceAll("\'","\\\\'").replaceAll("\"","&quot;");								
                            if (sql.length() > 25) {
                         %> <a href="javascript:;" onClick="window.open('showSql.jsp?name=<%=escapedSql%>','SqlQuery','height=200,width=400,location=no,resizable=yes,scrollbars=yes,toolbars=no')">
                         		<%=sql.substring(0,25)+"..."%> 
                         	</a>
                         <%
                            }
                            else {
                         %> <%=sql%> <%
                            }
                         %></td>

							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="avgTotalTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="avgExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="avgPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="noOfExecutes" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="noOfPrepares" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="maxExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="maxPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="minExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="minPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="firstExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="firstPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="lastExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="lastPrepareTime" /></td>
						</tr>

					</logic:iterate>
				</table>
				</td>
			</tr>

			<tr>
				<td class="lablel1Bold">Top <%=n%> SQL queries by Execution Count</td>
			</tr>

			<tr>
				<td class="noBorderCell">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="lablel1Bold" width="30%" rowspan="2">Sql Query</td>
						<td class="lablel1Bold" colspan="3">Avg. Time(ms)</td>
						<td class="lablel1Bold" colspan="2">Count</td>
						<td class="lablel1Bold" colspan="2">Max. Time(ms)</td>
						<td class="lablel1Bold" colspan="2">Min. Time(ms)</td>
						<td class="lablel1Bold" colspan="2">First Exec.Time(ms)</td>
						<td class="lablel1Bold" colspan="2">Last Exec.Time(ms)</td>
					</tr>
					<tr>
						<td class="lablel1Bold">Total</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
						<td class="lablel1Bold">Execute</td>
						<td class="lablel1Bold">Prepare</td>
					</tr>
					<logic:iterate id="sqlStats"
						collection="<%= request.getAttribute(\"sqlStatisticsByCount\") %>">
						<tr>
							<td
								class="wrappedTD <bean:write name="sqlStats" property="tdStyle"/>"
								width="30%"><%
                            String sql = ((SqlStatisticsFormBean)sqlStats).getSql();
							String escapedSql =sql.replaceAll("\'","\\\\'").replaceAll("\"","&quot;");
                            if (sql.length() > 25) {
                         %>
                         <a href="javascript:;" onClick="window.open('showSql.jsp?name=<%=escapedSql%>','SqlQuery','height=200,width=400,location=no,resizable=yes,scrollbars=yes,toolbars=no')"> 
                         <%=sql.substring(0,25)+"..."%> 
                         </a>
                         <%
                            }
                            else {
                         %> <%=sql%> <%
                            }
                         %></td>

							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="avgTotalTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="avgExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="avgPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="noOfExecutes" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="noOfPrepares" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="maxExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="maxPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="minExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="minPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="firstExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="firstPrepareTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="lastExecuteTime" /></td>
							<td
								class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write
								name="sqlStats" property="lastPrepareTime" /></td>
						</tr>

					</logic:iterate>
				</table>
				</td>
			</tr>
		</table>
</body>
<!-- InstanceEnd -->
</html>

