<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="uk.co.metoffice.util.MetoHelper"%>
<%@page import="uk.co.metoffice.beans.MetaData"%>
<%@ page import="java.util.List" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">


<head>
	<title>METO - Product details page</title>
	<jsp:include page="common/metadata.jsp" />
</head>

<%	List<MetaData> list = (List<MetaData>)request.getAttribute("list");	%>

<body id="home">
	<div id="wrap">
		<jsp:include page="common/header.jsp" />
		
		<div id="main-body">
			<div id="content">

				<!-- Page Content Starts Here -->
				<div class="mainheading" style="background: url(../img/browser.gif) no-repeat top right;">
				  <h1>Page H1 title</h1>
				</div>

				<div class="chapter">
					<p style="text-align:center;"><strong>Description text</strong></p>
					<p>What is this page doing.</p>
					<h3>Meto product criteria selection</h3>
				</div>

				<div class="chapter">
					<table id="customers">
						<tr>
							<th>Date</th>
							<th>Description</th>
							<th> </th>			
						</tr>			
						<%	for(MetaData temp : list){  
						
							 String validDate = MetoHelper.convertDateIntoString(temp.getValidityDate());
							 String url = "/meto/gcs/get/" + temp.getClientId().toLowerCase() + "::" 
							 				+ temp.getCategory().toLowerCase() + "::" 
											+ validDate.toLowerCase() + "::"+temp.getFileType().toLowerCase();
						%>
							<tr>
								<td><%=temp.getValidityDate() %></td>	
								<td><%=temp.getCategory() %></td>
								<td>
									<form action="<%= url %>">
					           			<input type="submit" value="Download" />
					        		</form>
								</td>
							</tr>	 

						<% } %>
					</table>
				</div>

			<!-- Page Content Ends Here -->
			</div> <!-- end #content -->
		</div> <!-- end #main-body -->

		<jsp:include page="common/rightsidebar.jsp" />
		<jsp:include page="common/footer.jsp" />
	</div> <!-- end #wrap -->
</body>
</html>
