<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="../css/jquery-ui.css" type="text/css" media="all" />
		<link rel="stylesheet" href="../css/base.css" type="text/css"/>
		
	</head>
	<body  >
		<div>
			Hello <%= session.getAttribute("username") %>
		</div>
		<div>
			<table>
				<tr>
					<td>
						<input type="button" name="HistoricalData" value = "Historical Data" onclick="document.location.href='jsp/datePicker.jsp'">
					</td>
					
					<td>
						<input type="button" name="HistoricalProducts" value="Historical Products" onclick="document.location.href='jsp/product.jsp'">
					</td>
				</tr>
			</table>	
		</div>
	</body>
</html>