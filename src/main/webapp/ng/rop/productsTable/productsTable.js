angular.module("productsTable",["models", "ngGrid"])
	.controller("productTable",["$scope", "ProductsData", function($scope, ProductsData){

		var downloadCellTmpl = '<div class="ngCellText" ng-class="col.colIndex()"><a href="{{row.getProperty(col.field)}}">{{row.getProperty("name")}}</a></div>';

		$scope.ProductsData = ProductsData;
		$scope.columnDefs = [];
		$scope.gridOptions = {data: 'ProductsData.data', columnDefs:[
			{field:"date", displayName:"Issued", cellFilter:"date"},
			{field:"name", displayName:"Product"},
			{field:"url", displayName:"Download", cellTemplate:downloadCellTmpl}
		]};
}])
	.directive("productsTable", function(){
		return {
			templateUrl:"rop/productsTable/productsTable.html",
			restrict:"E",
			replace: true,
			scope:{}
		}
	});
