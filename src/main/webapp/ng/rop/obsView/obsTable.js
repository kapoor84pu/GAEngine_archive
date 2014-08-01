angular.module("obsTable",["models", "ngGrid"])
	.controller("dataTable",["$scope", "TableData", function($scope, TableData){
		$scope.TableData = TableData;
		$scope.columnDefs = [];
		$scope.gridOptions = {data: 'TableData.data', columnDefs:'columnDefs'};

		$scope.$watch('TableData.data', function(newValue, oldValue) {
			var columnDefs = [];
			if(newValue instanceof Array && newValue.length > 0) {
				for (var col in newValue[0]){ if(newValue[0].hasOwnProperty(col)){
					var value = newValue[0][col];
					var colDef = {
						field:col
					};
					if (value instanceof Date) {
						colDef.cellFilter = "date"
					}
					columnDefs.push(colDef);
				}}
			}
			$scope.columnDefs = columnDefs;
		});


		/*$scope.myData = [{name: "Moroni", age: 50},
			{name: "Tiancum", age: 43},
			{name: "Jacob", age: 27},
			{name: "Nephi", age: 29},
			{name: "Enos", age: 34}];

		$scope.gridOptions = { data: 'myData' };*/
}])
	.directive("obsTable", function(){
		return {
			templateUrl:"rop/obsView/obsTable.html",
			restrict:"E",
			replace: true,
			scope:{}
		}
	})
