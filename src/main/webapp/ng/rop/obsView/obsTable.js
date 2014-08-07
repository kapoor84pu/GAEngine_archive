angular.module("obsTable",["models", "ngGrid"])
	.controller("dataTable",["$scope", "TableData", function($scope, TableData){
		$scope.SourceData = TableData;
		$scope.data = $scope.SourceData.data;
		$scope.columnDefs = [];
		$scope.gridOptions = {data: 'data', columnDefs:'columnDefs', showGroupPanel: false};
		// TODO: would be nice to turn on showGroupPanel but have to turn it off on pivot. Could do this via CSS as it doesn't seem to work using scope var.
		$scope.params = [
			{label:"None", value:""},
			{label:"Sun", value:"sun"}
		];
		$scope.pivotParam = "";

		// Update the view if the source data changes.
		$scope.$watch('SourceData.data', function() {
			updateView()
		});

		// Update the view if the pivotParam changes.
		$scope.$watch('pivotParam', function() {
			updateView()
		});


		function updateView(){
			// Update the table with any data or pivot changes
			if($scope.pivotParam === "" ){
				updateNonPivotView();
			} else {
				updatePivotView();
			}
		}

		function updateNonPivotView(){
			var newValue = $scope.SourceData.data, columnDefs = [];
			if(newValue instanceof Array && newValue.length > 0){
				for (var col in newValue[0]) {
					if (newValue[0].hasOwnProperty(col)) {
						var value = newValue[0][col];
						var colDef = {
							field: col
						};
						if (value instanceof Date) {
							colDef.cellFilter = "date"
						}
						columnDefs.push(colDef);
					}
				}
			}
			$scope.columnDefs = columnDefs;
			$scope.data = $scope.SourceData.data;
		}

		function updatePivotView(){
			var pivotMap = {}, pivotTable = [], regionNames = {};
			var newColDefs = [
				{field: "date", displayName: "Date", cellFilter: "date"}
			];
			// Pivot data into date groups
			for (var i = 0; i < $scope.data.length; i++) {
				var row = $scope.data[i];
				var newRow = pivotMap[String(row.date)] = (pivotMap[String(row.date)])? pivotMap[String(row.date)] : {date:row.date};
				newRow[row.regionName] = row[$scope.pivotParam];
				regionNames[row.regionName] = row.regionName;
			}
			// Put the pivot data in to array
			for (var date in pivotMap) { if (pivotMap.hasOwnProperty(date)){
				pivotTable.push(pivotMap[date]);
			}}
			// Add the regions to the col defs
			for (var region in regionNames) {
				if (regionNames.hasOwnProperty(region)) {
					newColDefs.push({field: region, displayName: region});
				}
			}
			// Update
			$scope.data = pivotTable;
			$scope.columnDefs = newColDefs;
		}
}])
	.directive("obsTable", function(){
		return {
			templateUrl:"rop/obsView/obsTable.html",
			restrict:"E",
			replace: true
		}
	});
