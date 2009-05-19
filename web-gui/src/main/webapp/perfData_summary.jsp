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

<%@page
	import="net.sf.infrared.web.util.ViewUtil,net.sf.infrared.base.model.LayerTime,java.util.Set,java.util.Iterator"
	contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>

<%@ taglib uri="struts/logic" prefix="logic"%>
<%@ taglib uri="struts/bean" prefix="bean"%>
<%@ taglib uri="infrared/infrared" prefix="infrared"%>
<%@ taglib uri="struts/html" prefix="html"%>

<%String[][] appInstMap = (String[][]) request.getAttribute("map");
            String sortBy = request.getParameter("sortBy");
            String sortDir = request.getParameter("sortDir");
            String sortByAbs = request.getParameter("sortByAbs");
            String sortDirAbs = request.getParameter("sortDirAbs");
%>
<html>
<!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<jsp:useBean id="isInActiveMode" scope="session"
	class="java.lang.String" />

<logic:notEqual name="isInActiveMode" value="false">

	<SCRIPT LANGUAGE="JavaScript" type="text/javascript">
        var noOfApplications = "<%=appInstMap.length%>";
        map =new Array(noOfApplications);

        function populateInstanceBox()
        {
            var i=0;
            var j =0;
            var form = document.application;
            for (i=form.instanceName.options.length; i >= 0; i--)
            {
                form.instanceName.options[i] = null;
            }
            var k=0;
            var tempArr1 = new Array();
            var tempArr2 = new Array();
            for(i=0;i< form.applicationName.options.length;i++)
            {
                if(form.applicationName.options[i].selected)
                {
                    for(j=0; j < map[i].length;j++)
                    {
                        tempArr1[k++] = map[i][j];
                    }
                }
            }
            tempArr1.sort();
            k=0;
            for(i=0; i < (tempArr1.length-1);i++)
            {
                if(tempArr1[i] != tempArr1[i+1])
                {
                    tempArr2[k++] = tempArr1[i];
                }
            }
            tempArr2[k] = tempArr1[tempArr1.length-1];

            for(i=0; i < tempArr2.length;i++)
            {
                form.instanceName.options[i] = new Option(tempArr2[i]);
            }
        }
    </SCRIPT>
</logic:notEqual>


<SCRIPT SRC="javascript/functions.js" type="text/javascript"></SCRIPT>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet"
	type="text/css">
</head>

<body>

<logic:notEqual name="isInActiveMode" value="false">
	<script type="text/javascript">
    var k =0;
    var l =0;
<%
    if(appInstMap != null)
    {
%>
<%      for(int i=0;i<appInstMap.length;i++)
        {
%>          l=0;
            map[k]=new Array("<%=appInstMap[i].length%>");
<%          for(int j=0;j<appInstMap[i].length;j++)
            {
%>              map[k][l++] ="<%=appInstMap[i][j]%>";
<%
            }
%>          k++;
<%      }
    }
%>
</script>
</logic:notEqual>

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
		<%request.setAttribute("highlight", "summary");%> <jsp:include
			page="includes/tabDetails.jsp"  /> <!-- InstanceEndEditable --></td>
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


	<logic:notEqual name="isInActiveMode" value="false">

		<tr>
			<td class="pageTitle">
			<form name="application" ACTION="perfData_summaryAction.do"
				METHOD="GET" id="applicationInstanceRow">Application: &nbsp;&nbsp; 
			<select name="applicationName" MULTIPLE onchange="return populateInstanceBox()">
				<logic:iterate id="appName" name="perfBean" property="applicationName">
					<infrared:contains name="perfBean" property="selectedApplications" selectedName="appName" notContains="false">
						<option selected><bean:write name="appName" /></option>
					</infrared:contains>

					<infrared:contains name="perfBean" property="selectedApplications" selectedName="appName" notContains="true">
						<option><bean:write name="appName" /></option>
					</infrared:contains>
				</logic:iterate>
			</select> 
			&nbsp;Instance: &nbsp; 
			<select name="instanceName" MULTIPLE>
				<logic:iterate id="instName" name="perfBean" property="instanceName">
					<infrared:contains name="perfBean" property="selectedInstances" selectedName="instName" notContains="false">
						<option selected><bean:write name="instName" /></option>
					</infrared:contains>

					<infrared:contains name="perfBean" property="selectedInstances" selectedName="instName" notContains="true">
						<option><bean:write name="instName" /></option>
					</infrared:contains>
				</logic:iterate>

			</select> &nbsp;&nbsp;<input type="submit" value="GO" align="center">
			</form>
			</td>
		</tr>
	</logic:notEqual>
	<logic:equal name="isInActiveMode" value="false">
		<jsp:include page="includes/applicationNameInfo.jsp"  />
	</logic:equal>


	<tr>
		<logic:notEqual name="isInActiveMode" value="false">
			<td class="pageTitle">The application is in active mode</td>
		</logic:notEqual>

		<logic:equal name="isInActiveMode" value="false">
			<td class="pageTitle">The data loaded is from the DB </td>
		</logic:equal>
	</tr>
	<tr>
		<td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="lablel1Bold">Summary of all modules</td>
						<logic:notEqual name="isInActiveMode" value="false">
							<td class="lablel1Bold" align="right"><a
								href='<%=response.encodeURL("perfData_summaryAction.do?reset=true")%>'>Reset</a></td>
							<td class="lablel1Bold" align="right"><a
								href='<%=response.encodeURL("perfData_summaryAction.do?refresh=true")%>'>Refresh</a></td>
						</logic:notEqual>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr>
				<td class="noBorderCell">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="noBorderCell" width="33%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%" nowrap class="lablel1Bold">Absolute Module</td>
								<td nowrap class="lablel1"><a
									href='<%=response.encodeURL(ViewUtil.getSortByHrefInAbsSummary("name",sortDirAbs,sortByAbs))%>'>
								<img
									src='<%=ViewUtil.getSortByIconInSummary("name",sortDirAbs,sortByAbs)%>'
									width="15" height="15" border="0" alt=""> </a></td>
							</tr>
						</table>
						</td>
						<td width="33%" class="noBorderCell">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%" nowrap class="lablel1Bold">Total Time (ms)</td>
								<td nowrap class="lablel1"><a
									href='<%=response.encodeURL(ViewUtil.getSortByHrefInAbsSummary("time",sortDirAbs,sortByAbs))%>'>
								<img
									src='<%=ViewUtil.getSortByIconInSummary("time",sortDirAbs,sortByAbs)%>'
									width="15" height="15" border="0" alt=""> </a></td>
							</tr>
						</table>
						</td>
					</tr>
					<logic:iterate id="layerTime" name="perfBean" property="absoluteLayerTimes">
						<tr>
							<td class="wrappedTD">
							<bean:define id="lname" name="layerTime" property="layer" type="java.lang.String"/>
							
							 <a href="<%=response.encodeURL("perfData_layerSummaryAction.do?type="+lname+"&layerType=abs")%>">
                        		<bean:write name="layerTime" property="layer"/>
                			</a></td>
							<td class="numericTD"><bean:write name="layerTime" property="time" /></td>
						</tr>
					</logic:iterate>
				</table>
				</td>
			</tr>
			<tr><td>&nbsp;&nbsp</td></tr>
			
			<tr>
				<td class="noBorderCell">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="noBorderCell" width="33%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%" nowrap class="lablel1Bold">Hierarchical Module</td>
								<td nowrap class="lablel1"><a
									href='<%=response.encodeURL(ViewUtil.getSortByHrefInSummary("name",sortDir,sortBy))%>'>
								<img
									src='<%=ViewUtil.getSortByIconInSummary("name",sortDir,sortBy)%>'
									width="15" height="15" border="0" alt=""> </a></td>
							</tr>
						</table>
						</td>
						<td width="33%" class="noBorderCell">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%" nowrap class="lablel1Bold">Total Time (ms)</td>
								<td nowrap class="lablel1"><a
									href='<%=response.encodeURL(ViewUtil.getSortByHrefInSummary("time",sortDir,sortBy))%>'>
								<img
									src='<%=ViewUtil.getSortByIconInSummary("time",sortDir,sortBy)%>'
									width="15" height="15" border="0" alt=""> </a></td>
							</tr>
						</table>
						</td>
					</tr>
					<logic:iterate id="layerTime" name="perfBean" property="layerTimes">
						<tr>
							<td class="wrappedTD">
							<bean:define id="lname2" name="layerTime" property="layer" type="java.lang.String" />
							
							 <a href="<%=response.encodeURL("perfData_layerSummaryAction.do?type="+lname2+"&layerType=hier")%>">
                        		<bean:write name="layerTime" property="layer"/>
                			</a></td>
							<td class="numericTD"><bean:write name="layerTime" property="time" /></td>
						</tr>
					</logic:iterate>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>

</table>
</body>
<!-- InstanceEnd -->
</html>
