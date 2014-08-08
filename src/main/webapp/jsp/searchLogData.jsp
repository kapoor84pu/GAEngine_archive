<%@ page language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<head>
	<title>METO - Product criteria page</title>
	<jsp:include page="common/metadata.jsp" />
</head>
<%
	String fromDate = (String)request.getAttribute("fromLogDateTime");
	if (fromDate == null){
		fromDate = "";
	}
	String toDate = (String)request.getAttribute("toLogDateTime");
	if (toDate == null){
		toDate = "";
	}
	String logContent = (String)request.getAttribute("LogContent");
	if (logContent == null){
		logContent = "";
	}

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
					
						<div class="base">
							<div class="leftBase">
								<form action="/meto/Logs/All" method="post" id="logform">
									<table id="tblTest">
										 <tr>
											 <td>From Date : </td>
											 <td><input type="text" id="fromLogDateTime" value="<%=fromDate %>" name="fromLogDateTime"  > </input></td>
										 </tr>
										 <tr>	 
											 <td>To Date : </td>
											 <td><input type="text" id="toLogDateTime" value="<%=toDate %>" name="toLogDateTime"  /></td>
										 </tr>
										 <tr>	 
											 <td></td>
											 <td></td>
										 </tr>
										 <tr>	 
											 <td><input type="submit" value="Continue" name="Continue"></input></td>
											 <td><input type="submit" value="Download" name="Download"></input></td>
										 </tr>										 
	 								</table>
								</form>	
								<br/> <br/>
								
								<%= logContent %>
							</div>
							<div class="rightBase">
							</div>

						</div>

				</div>

			<!-- Page Content Ends Here -->
			</div> <!-- end #content -->
		</div> <!-- end #main-body -->

		<jsp:include page="common/rightsidebar.jsp" />
		<jsp:include page="common/footer.jsp" />
	</div> <!-- end #wrap -->
</body>
</html>