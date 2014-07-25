<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="uk.co.metoffice.beans.MetoDataJPA"%>
<%@page import="java.util.List"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title>METO - Product criteria page</title>
	<jsp:include page="common/metadata.jsp" />
</head>
<%
	List<MetoDataJPA> list = (List<MetoDataJPA>)request.getAttribute("list");
	String fromDate = ((String)request.getAttribute("fromDate")).replaceAll("/", "");
	String toDate = ((String)request.getAttribute("toDate")).replaceAll("/", "");
	String regions = (String)request.getAttribute("regionList");  
	String url = "/meto/WeatherData/" + fromDate + "/" + toDate + "/" + regions;	
%>
		
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
							<th>Regions</th>
							<th>Max Temp (°C)</th>
							<th>Pressure</th>
							<th>Max Temp day</th>
							<th>Min Temp day</th>
							<th>Max feels like day</th>
							<th>Min Temp night</th>
							<th>Sunshine hours</th>
							<th>Snow amount</th>
						</tr>					
						<%	for(MetoDataJPA temp : list){  %>
						<tr>
							<td><%=temp.getWeatherDate() %></td>
							<td><%=temp.getRegions() %></td>
							<td><%=temp.getTemperature() %></td>
							<td><%=temp.getPressure() %></td>
							<td> </td>
							<td> </td>
							<td> </td>
							<td> </td>
							<td> </td>
							<td> </td>
						</tr>
						<% } %>
					</table>
		
					
					<br/><br/>
		
					<table>
						<tr class="lowerTable">
							<td>
								<form action="<%= url %>">
		                   			<input type="submit" value="Export data"> </input>
		                		</form>
							</td>
							<td>
								<form action="/">
		                   			<input type="submit" value="Email to user"> </input>
		                		</form>
							</td>
							<td>
								<form action="/">
		                   			<input type="submit" value="Print"> </input>
		                		</form>
							</td>														
						</tr>
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

