
<%@ page import="com.nee.beans.MetaData" %>
<%@ page import="com.nee.utils.MetoHelper" %>
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
		<%	List<MetaData> list = (List<MetaData>)request.getAttribute("list");	%>
		<table>
			<tr>
				<th>Date</th>
				<th>Description</th>			
			</tr>			
			<%	for(MetaData temp : list){  %>
			<tr>
				<td><%=temp.getValidityDate() %></td>	
				<td><%=temp.getCategory() %></td>
			</tr>	 
			<% 
				 String  validDate = MetoHelper.convertDateIntoString(temp.getValidityDate());
			String url = "/meto/gcs/get/" + temp.getClientId().toLowerCase() + "::" + temp.getCategory().toLowerCase() + "::" +
					 validDate.toLowerCase() + "::"+temp.getFileType().toLowerCase();
					 
					 		
        	%>	
        	<td>
				<form action="<%= url %>">
           			<input type="submit" value="Download">
        		</form>
			</td>
			<% } %>
		</table>
	</body>
</html>

