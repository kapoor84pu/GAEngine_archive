<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%@ page import="java.util.*" %>
<head>
	<title>METO - Welcome 02</title>
	<jsp:include page="common/metadata.jsp" />
</head>

<%
	// Get Cookies and check if the cookie is present

	// If not
	Cookie cookie = new Cookie("RetailPortal","2");
	response.addCookie(cookie);
	
	//Cookie cookie = new Cookie("RetailPortal","ClientID=2");
	//response.addCookie(cookie);
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
					<h3>Meto table data</h3>
				</div>

				<div class="chapter">
					<table>
						<tr>
							<td>
								<input type="button" name="HistoricalData" value = "View Historical Data" onclick="document.location.href='jsp/searchHistoricalData.jsp'" />
							</td>
							<td>
								<input type="button" name="HistoricalProducts" value="View Historical Products" onclick="document.location.href='jsp/searchHistoricalProduct.jsp'" />
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
