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
	import="java.util.ArrayList,
			net.sf.infrared.web.util.ViewUtil,
			net.sf.infrared.base.model.AggregateOperationTree,
			net.sf.infrared.base.model.AggregateExecutionTime,
			net.sf.infrared.web.util.SqlStatistics,
			net.sf.infrared.web.treecontrolmodel.TreeFactoryImpl,
			net.sf.jsptree.tree.TreeFactory,
			net.sf.jsptree.skin.AbstractSkin,
			net.sf.infrared.base.util.TreeNode,
			net.sf.infrared.web.util.PerformanceDataSnapshot"
	contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>

<%@ taglib uri="struts/bean" prefix="bean"%>
<%@ taglib uri="struts/html" prefix="html"%>
<%@ taglib uri="struts/logic" prefix="logic"%>
<%@ taglib uri="jsptree/jsptree" prefix="sf"%>

<%
    String apiName = (String)session.getAttribute("api");
    String apiType = (String)session.getAttribute("type");
	String layerName = (String)session.getAttribute("layerName");
    String mergeTree = "";

    TreeNode mergedHead = (TreeNode)session.getAttribute("mergedHead");
    AggregateExecutionTime[] mergedTreeJdbcSummaries = null;
    SqlStatistics[] mergedTreeSqlStatistics = null;

    PerformanceDataSnapshot perfData = null;
    String sqlTableName = null;
    String collapsedRowName = null;
    int colSpan = 4;
    boolean fetchEnabled= true;
    if(mergedHead != null){
        mergeTree = "exist";
        mergedTreeJdbcSummaries = ViewUtil.getJDBCSummary(mergedHead);
        mergedTreeSqlStatistics = ViewUtil.getSqlStatistics(mergedHead);
    }

    pageContext.setAttribute("mergeTreeExist",mergeTree );

    String expandMode = "multiple";
    String expandIcon = "fw/def/image/tree/Expand.gif";
    String expandIconHref = "perfData_apiSumm_callTraces.jsp?expandMode=full";
    if("full".equals(request.getParameter("expandMode")))
    {
        expandMode = "full";
        expandIcon = "fw/def/image/tree/Contract.gif";
        expandIconHref = "perfData_apiSumm_callTraces.jsp?expandMode=multiple";
    }

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
</script>
</head>

<body>
<form
	action="<%=response.encodeURL("perfData_apiSumm_callTracesAction.do")%>"
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
		<%
            request.setAttribute("highlight","summary");
        %> <jsp:include page="includes/tabDetails.jsp" /> <!-- InstanceEndEditable --></td>
	</tr>
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="secondNav" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" nowrap class="subMenuUp">&nbsp;</td>
				<td nowrap class="subMenuDown">Operation Summary</td>
				<%--            <logic:notEqual parameter="api" value="JDBC">--%>
				<%--                <td nowrap class="menuUp"><a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName +"&type="+apiType)%>" class="menu">JDBC Summary</a></td>--%>
				<%--            </logic:notEqual>--%>
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
		<logic:notEqual name="isInActiveMode" value="false">
			<td class="pageTitle">The application is in active mode</td>
		</logic:notEqual>

		<logic:equal name="isInActiveMode" value="false">
			<td class="pageTitle">The data loaded is from the DB</td>
		</logic:equal>
	<tr>
		<td class="moduleTitle"><!-- InstanceBeginEditable name="pageTitle" -->Summary
		of operations involving <%=apiName%><!-- InstanceEndEditable --></td>
	</tr>
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="lablel1Bold"><a
							href="<%=response.encodeURL("perfData_layerSummaryAction.do?type="+apiType)%>">Go
						Back To Summary Of Module <%=apiType%></td>
						<logic:notEqual name="isInActiveMode" value="false">
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do?reset=true")%>">Reset</a></td>
							<td class="lablel1Bold" align="right"><a
								href="<%=response.encodeURL("perfData_summaryAction.do")%>">Refresh</a></td>
						</logic:notEqual>

					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>

			<jsp:useBean id="mergeTreeExist" scope="page" class="java.lang.String" />
			<logic:equal name="mergeTreeExist" value="exist">

				<tr>
					<td class="lablel1Bold">Merged Call Tree</td>
				</tr>
				<tr>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="3">
					<%
                        String imagePath = request.getContextPath()+"/imgs/tree/menu/blue/";
                        TreeFactory treeFactory = (TreeFactory)session.getAttribute("treeFactory");
                        pageContext.setAttribute("treeFactory", treeFactory);
                        String treeName = "InfraTree";
                     %> 
                     <a href="<%=response.encodeURL(expandIconHref)%>"> 
                     		<img src="<%=expandIcon%>" width="10" height="10" border="0" />
                     </a> 
                     <sf:JSPTree
						name="<%=treeName%>" 
						treeFactory="treeFactory" 
						startAtDepth="0"
						imagesPath="<%=imagePath%>"
						templatePath="net/sf/jsptree/example/template/menu/"
						shareTreeStructure="false" expandMode="<%=expandMode%>"
						skin="<%=AbstractSkin.MENU_SKIN%>"
					 />
							</td>
                <%
                    Integer jdbcCount = new Integer(mergedTreeJdbcSummaries.length);
                    Integer sqlCount = new Integer(mergedTreeSqlStatistics.length);
                    pageContext.setAttribute("jdbcSummCount", jdbcCount);
                    pageContext.setAttribute("sqlStatsCount", sqlCount);
                %>
                <!--jsp:useBean id="jdbcSummCount" scope="page" class="java.lang.Integer"/-->
                <!--logic:greaterThan name="jdbcSummCount" value="0"-->
                <%if (mergedTreeJdbcSummaries.length > 0) {%>
                <tr>
                    <td class="lablel1Bold" colspan="3">JDBC Summary</td>
                </tr>

                <td class="noBorderCell" width="33%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">API Name</td>
                    </tr>
                  </table></td>
                <td width="33%" class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">Average Time (ms)</td>
                    </tr>
                  </table></td>
                <td width="33%" class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">Count</td>
                    </tr>
                  </table></td>
              </tr>

                <logic:iterate id="aggregateApiTime" collection="<%= session.getAttribute(\"jdbcSummaries\") %>">
                 <tr>
                    <td><bean:write name="aggregateApiTime" property="apiName"/></td>
                    <td><bean:write name="aggregateApiTime" property="averageInclusiveTime"/></td>
                    <td><bean:write name="aggregateApiTime" property="executionCount"/></td>
                </tr>
                </logic:iterate>
                <!--/logic:greaterThan-->
			 <%
				}
			 %>

            </table></td>
        </tr>
        <tr><td>&nbsp;</td></tr>
		<%if (mergedTreeSqlStatistics.length > 0) {%>
        <!--jsp:useBean id="sqlStatsCount" scope="page" class="java.lang.Integer"/-->
        <!--logic:greaterThan name="sqlStatsCount" value="0"-->
       <%
                    int n = 5;
                    request.setAttribute("count",new Long(1));
       %>
       <tr>
      <tr>
      <td class="lablel1Bold"> Top <%=n%> SQL queries by Average Execution Time </td>
      </tr>
      <%
            request.setAttribute("sqlTableName","topNByAvgExecTime");
            request.setAttribute("collapsedRowName","topNByAvgExecTimeCollpasedRow");
            ArrayList topNQueriesByExecution = (ArrayList)session.getAttribute("sqlStatisticsByExec");

            request.setAttribute("sqlStatistics",topNQueriesByExecution);
      %>
      <%@ include file="includes/sqlStatistics.jsp" %>
      <tr></tr>
      <tr>
      <td class="lablel1Bold"> Top <%=n%> SQL queries by Execution Count </td>
      </tr>
      <%
            request.setAttribute("sqlTableName","topNByAvgExecCount");
            request.setAttribute("collapsedRowName","topNByAvgExecCountCollpasedRow");
            ArrayList topNQueriesByCount = (ArrayList)session.getAttribute("sqlStatisticsByCount");
            request.setAttribute("sqlStatistics",topNQueriesByCount);
      %>
      <%@ include file="includes/sqlStatistics.jsp" %>
        <!--/logic:greaterThan-->
		<%
			}
		%>

         <tr><td>&nbsp;</td></tr>
        
							
		</table>
		</td>
	</tr>
	</logic:equal>
	<logic:notEqual name="mergeTreeExist" value="exist">
		<p align="center">Call sequence not available for API <%=apiName%></p>
	</logic:notEqual>

</table>
<form>
</body>
<!-- InstanceEnd -->
</html>

