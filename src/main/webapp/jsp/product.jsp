<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="../css/jquery-ui.css" type="text/css" media="all" />
		<link rel="stylesheet" href="../css/base.css" type="text/css"/>
		<script src="../js/jquery.min.js" type="text/javascript"></script>
		<script src="../js/jquery-ui.min.js" type="text/javascript"></script> 
		<script type="text/javascript">
			$(function () {
				$('#fromDate').datepicker({ dateFormat: 'yy/mm/dd' });
				$('#fromDate').datepicker();
				$('#toDate').datepicker({ dateFormat: 'yy/mm/dd' });
				$('#toDate').datepicker();
			});
		</script>
		<style>
			.ui-datepicker th { background-color: #CCCCFF; }
		</style>
	</head>
	<body>
		<form action="/meto/WeatherData/Products" method="post">
			<div>
				<table>
					 <tr>
						 <td>From Date : </td>
						 <td><input type="text" id="fromDate" value="" name="fromDate"  /></td>
						 <td>To Date : </td>
						 <td><input type="text" id="toDate" value="" name="toDate"  /></td>
						 <td><input type="submit" value="Continue"><br></td>
					 </tr>
				</table>
			</div>
		</form>
	</body>
</html>

							