
<%@ page import="com.nee.beans.MetoDataJPA" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="../css/tableCss.css" type="text/css"/>
		<script>
			$(document).ready(function(){
	   			$("tr:even").addClass("even");
	  			$("tr:odd").addClass("odd");
			});
		</script>
	</head>
	<body>
		<%
			
			List<MetoDataJPA> list = (List<MetoDataJPA>)request.getAttribute("list");
			
		%>
		<div class="mainResult">
			<div class="result">
				<table class="tableCss">
					<tr>
						<th>Date</th>
						<th>Regions</th>
						<th>Max Temperature(Deg C)</th>
						<th>Pressure</th>
					</tr>
					<%	for(MetoDataJPA temp : list){  %>
		
					
					<tr>
						<td><%=temp.getDate() %></td>
						<td><%=temp.getRegions() %></td>
						<td><%=temp.getTemperature() %></td>
						<td><%=temp.getPressure() %></td>
						
					</tr>
					<% } %>
					
				</table>
			</div><br><br>
			<div>
				<table>
					<tr class="lowerTable">
						<td>Export Data</td><td>Email to user</td><td>Print</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html>