

$(function () {
	$('#fromDate').datepicker({ dateFormat: 'yy/mm/dd' });
	$('#fromDate').datepicker();
	$('#toDate').datepicker({ dateFormat: 'yy/mm/dd' });
	$('#toDate').datepicker();
	$('#fromLogDateTime').datetimepicker();
	$('#toLogDateTime').datetimepicker();
});

var checked = false;
function checkedAll () {
	if (checked == false){
		checked = true
	}else{
		checked = false
	}
	for (var i = 0; i < document.getElementById('myform').elements.length; i++) {
			document.getElementById('myform').elements[i].checked = checked;
	}
}