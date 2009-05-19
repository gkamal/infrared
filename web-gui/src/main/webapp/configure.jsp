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
 * Contributor(s):   ;
 *
-->

<%@ page contentType="text/html; charset=iso-8859-1" language="java"
	errorPage=""%>
<%@ taglib uri="struts/html" prefix="html"%>
<html>
<head>

<title>InfraRED</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<link href="common/stylesheets/master.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="common/stylesheets/dynCalendar.css"
	type="text/css" media="screen">
<script src="common/javascript/browserSniffer.js" type="text/javascript"
	language="javascript"></script>
<script src="common/javascript/dynCalendar.js" type="text/javascript"
	language="javascript"></script>

<script type="text/javascript">
	<!--
		// Calendar callback. When a date is clicked on the calendar
		// this function is called so you can do as you want with it
		function calendarCallbackForStartDate(date, month, year)
		{
			date = date + '/' + month + '/' + year;
			document.forms[0].startDate.value = date;
		}

		function calendarCallbackForEndDate(date, month, year)
		{
			date = date + '/' + month + '/' + year;
			document.forms[0].endDate.value = date;
		}
	// -->
	</script>

</head>

<body>
<html:form action="perfData_summaryAction">
	<table width="780" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="noBorderCell" colspan="3">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="50%" class="tavantLogo">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="noBorderCell" colspan="3"><%request.setAttribute("highlight", "configuration");

		%> <jsp:include page="includes/tabDetails.jsp" /></td>
		</tr>
		<tr>
			<td class="noBorderCell"></td>
		</tr>
		<tr>
			<td class="noBorderCell"><!-- InstanceBeginEditable name="Error" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			</table>
			</td>
		</tr>
		<tr>
		<tr>
			<td class="pageTitle" colspan="3">Configure</td>
		</tr>
		<tr>
		<td class="noBorderCell"></td>
		</tr>
		<tr>
			<td class="text_normal" colspan="3"><html:radio property="dataFetch" value="true" /><b>Live
			Data</b></td>
		</tr>
		<tr>
			<td class="text_normal"><html:radio property="dataFetch" value="false" />
				<b>DB Data</b>
			</td>
			<td colspan="1"> <b> Start Date </b><html:text property="startDate" readonly="true"/> 
			<script language="JavaScript" type="text/javascript">
    <!--
    	startDateCalendar = new dynCalendar('startDateCalendar', 'calendarCallbackForStartDate', 'images/');
    //-->
    			</script></td>
			<td colspan="1"><b> End Date </b><html:text property="endDate" readonly="true"/> 
			<script language="JavaScript" type="text/javascript">
    <!--
    	endDateCalendar = new dynCalendar('endDateCalendar', 'calendarCallbackForEndDate', 'images/');
    //-->
    			</script></td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;&nbsp;<html:submit value="GO" /></td>
		</tr>

	</table>
</html:form>
</body>
</html>
