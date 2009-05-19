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
	import="net.sf.infrared.web.util.ViewUtil,
			net.sf.infrared.web.util.PerformanceDataSnapshot,
			java.util.HashMap,
			java.util.ArrayList,
			org.apache.commons.beanutils.PropertyUtils,
			net.sf.infrared.base.model.ExecutionContext,
			net.sf.infrared.web.formbean.AggrExecTimeFormBean"
	contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>

<%@ taglib uri="struts/logic" prefix="logic"%>
<%@ taglib uri="struts/bean" prefix="bean"%>
<%@ taglib uri="infrared/infrared" prefix="infrared"%>

<%

    String exclInclMode = null;
    exclInclMode = (String)session.getAttribute("exclusiveInclusiveMode");
    String sortBy = request.getParameter("sortBy");
    String sortDir = request.getParameter("sortDir");
    if(exclInclMode == null)
        exclInclMode = "inclusive";

    PerformanceDataSnapshot perfData = null;
    String sqlTableName = null;
    String collapsedRowName = null;
    int colSpan = 4;
    boolean fetchEnabled= true;

    pageContext.setAttribute("exclusiveInclusiveMode", exclInclMode);
    String type = (String)request.getAttribute("apiType");
    String tempSortBy = "adjAvg";
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
		<td class="noBorderCell"><!-- InstanceBeginEditable name="secondNav" -->
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

		<jsp:useBean id="exclusiveInclusiveMode" scope="page"
			type="java.lang.String" />
		<td class="moduleTitle"><!-- InstanceBeginEditable name="pageTitle" -->Summary
		of module: <%=apiType%> (<%=exclusiveInclusiveMode%>)<!-- InstanceEndEditable -->
		&nbsp;&nbsp;&nbsp; <logic:equal name="exclusiveInclusiveMode"
			value="inclusive">

			<a
				href="<%=response.encodeURL("perfData_layerSummaryAction.do?type="+apiType+"&sortBy=adjAvgExclusive&sortDir=desc&exclusiveInclusiveMode=exclusive")%>">
			(Switch to exclusive mode)</a>
		</logic:equal> <logic:notEqual name="exclusiveInclusiveMode"
			value="inclusive">

			<a
				href="<%=response.encodeURL("perfData_layerSummaryAction.do?type="+apiType+"&sortBy=adjAvg&sortDir=desc&exclusiveInclusiveMode=inclusive")%>">
			(Switch to inclusive mode)</a>

		</logic:notEqual></td>
	</tr>
	<td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="lablel1Bold"><a href="perfData_summaryAction.do">Go Back
					To Summary Of All Modules</a></td>
					<logic:equal name="isInActiveMode" value="true">

						<logic:notEqual name="exclusiveInclusiveMode" value="inclusive">
							<%
                 tempSortBy = "adjAvgExclusive";
            %>

						</logic:notEqual>
						<td class="lablel1Bold" align="right"><a
							href="<%=response.encodeURL("perfData_summaryAction.do?reset=true")%>">
						Reset</a></td>
						<td class="lablel1Bold" aling="right"><a
							href="<%=response.encodeURL("perfData_summaryAction.do")%>">
						Refresh</a></td>
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
		<tr id="apiNameTable">
			<td class="noBorderCell">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="80%" nowrap class="lablel1Bold"><img
								src="fw/def/image/tree/15/16.gif"
								onClick="showHide('apiNameTable','extraLine')"> &nbsp;Operation Name
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("name",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("name",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">Total Time<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("totalTime",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("totalTime",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td nowrap class="lablel1Bold">Count<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("count",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("count",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">Avg.<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("time",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("time",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">Adj. Avg.<sup>*</sup><!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("adjAvg",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("adjAvg",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">Min.<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("minTime",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("minTime",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">Max.<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("maxTime",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("maxTime",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">First<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("first",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("first",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
					<td class="noBorderCell">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="lablel1Bold">Last<!--/td--> <a
								href="<%=response.encodeURL(ViewUtil.getSortByHrefInLayer("lastExecTime",sortDir,sortBy,apiType,exclusiveInclusiveMode))%>">
							<img
								src="<%=ViewUtil.getSortByIconInLayer("lastExecTime",sortDir,sortBy,exclusiveInclusiveMode)%>"
								width="15" height="15" border="0"> </a></td>
						</tr>
					</table>
					</td>
				</tr>

				<logic:iterate id="aggregateApiTime"
					collection="<%=request.getAttribute(\"aggrApiList\")%>">
					<%
                  String tdStyle = "";
                  String propertyName = "apiName";
				  AggrExecTimeFormBean execTimeBean = (AggrExecTimeFormBean) aggregateApiTime;
				  String apiName = execTimeBean.getApiName();
				  String ctxClass = execTimeBean.getCtxName();
				  String actualLayer = execTimeBean.getContext().getLayer();

              %>

					<tr>

						<logic:equal name="exclusiveInclusiveMode" value="inclusive">
							<infrared:colorThreshold name="aggregateApiTime"
								property="averageInclusiveTime">
								<%
							tdStyle = "hotSpot";
						%>
							</infrared:colorThreshold>


							<td class="wrappedTD <%=tdStyle%>"><a
								href="<%=response.encodeURL("perfData_apiSumm_callTracesAction.do?api="+apiName+"&type="+apiType+"&ctx="+ctxClass+"&layerName="+actualLayer)%>">
							<acronym title="<%=apiName%>"> <bean:write
								name="aggregateApiTime" property="truncatedName" /> </acronym> </a>
							</td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="totalInclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="executionCount" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="averageInclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="adjAverageInclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="minInclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="maxInclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="inclusiveFirstExecutionTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="inclusiveLastExecutionTime" /></td>

						</logic:equal>
						<logic:notEqual name="exclusiveInclusiveMode" value="inclusive">
							<infrared:colorThreshold name="aggregateApiTime"
								property="averageExclusiveTime">
								<%
							tdStyle = "hotSpot";
						%>
							</infrared:colorThreshold>

							<td class="wrappedTD <%=tdStyle%>"><a
								href="<%=response.encodeURL("perfData_apiSumm_callTracesAction.do?api="+apiName+"&type="+apiType+"&ctx="+ctxClass+"&layerName="+actualLayer)%>">
							<acronym title="<%=apiName%>"> <bean:write
								name="aggregateApiTime" property="truncatedName" /> </acronym> </a>
							</td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="totalExclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="executionCount" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="averageExclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="adjAverageExclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="minExclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="maxExclusiveTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="exclusiveFirstExecutionTime" /></td>
							<td class="numericTD <%=tdStyle%>"><bean:write
								name="aggregateApiTime" property="exclusiveLastExecutionTime" /></td>

						</logic:notEqual>
					</tr>

				</logic:iterate>
				<tr>
					<td colspan="9">* Adj. Avg - Adjusted average (average excluding
					the first execution)</td>
				</tr>
				<tr>
					<td colspan="9">* All times are in milliseconds</td>
				</tr>
				
			</table>
			</td>
		</tr>
		<!-- InstanceEndEditable -->
	</td>
	</tr>
</table>
</body>
<!-- InstanceEnd -->
</html>
