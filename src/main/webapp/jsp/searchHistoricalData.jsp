<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title>METO - Product criteria page</title>
	<jsp:include page="common/metadata.jsp" />
</head>

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
					<form action="/meto/WeatherData/All" method="post" id="myform">
						<div class="base">
							<div class="leftBase">
								<table id="tblTest">
									 <tr>
										 <td>From Date : </td>
										 <td><input type="text" id="fromDate" value="" name="fromDate"  /></td>
									 </tr>
									 <tr>	 
										 <td>To Date : </td>
										 <td><input type="text" id="toDate" value="" name="toDate"  /></td>
									 </tr>
									 <tr>
                     <div>
                       <input type="checkbox" name="day" value="Monday">Monday</input><br/>
                       <input type="checkbox" name="day" value="Tuesday">Tuesday</input><br/>
                       <input type="checkbox" name="day" value="Wednesday">Wednesday</input><br/>
                       <input type="checkbox" name="day" value="Thursday">Thursday</input><br/>
                       <input type="checkbox" name="day" value="Friday">Friday</input><br/>
                     </div>
                   </tr>
								</table>
							</div>

							<div class="rightBase">
								<input type="checkbox" value="ALL" onclick="checkedAll();">All Regions </input> <br/>
								<input type="checkbox" name="regions" value="LON">London</input><br/>
								<input type="checkbox" name="regions" value="SW_ENG">South West England</input><br/>
								<input type="checkbox" name="regions" value="NE_ENG">North East England</input><br/>
								<input type="checkbox" name="regions" value="NW_ENG_N_WAL">North West England and North Wales</input><br/>
								<input type="checkbox" name="regions" value="SC_ENG">South Central England</input><br/>
								<input type="checkbox" name="regions" value="C_SCO">Scotland Central</input><br/>
								<input type="checkbox" name="regions" value="E_ENG">Eastern England</input><br/>
								<input type="checkbox" name="regions" value="N_SCO">Scotland North</input><br/>
								<input type="checkbox" name="regions" value="S_WAL">South Wales</input><br/>
								<input type="checkbox" name="regions" value="N_IRE">Northern Ireland</input><br/>
								<br/><br/>
								<input type="submit" value="Continue"> </input><br/>
							</div>
							
						</div>
					</form>
				</div>

			<!-- Page Content Ends Here -->
			</div> <!-- end #content -->
		</div> <!-- end #main-body -->

		<jsp:include page="common/rightsidebar.jsp" />
		<jsp:include page="common/footer.jsp" />
	</div> <!-- end #wrap -->
</body>
</html>