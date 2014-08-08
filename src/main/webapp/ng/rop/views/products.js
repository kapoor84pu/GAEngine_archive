angular.module("products", ["productsTable", "productsService"])
	.controller("ProductsCtrl", ["$scope" , "productsService", function($scope, productsService){
		var ONE_WEEK =  1000 * 60 * 60 * 24 * 7;
		var NOW = (new Date()).getTime();
		$scope.dates = {from: new Date(NOW), to: new Date(NOW + ONE_WEEK)};

		$scope.submit = function(){
			productsService.getObs($scope.dates);
		}
	}]);