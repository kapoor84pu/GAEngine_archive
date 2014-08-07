angular.module("ropApp", ["queryBar", "ngRoute", "obsTable", "historicData", "products"])
	.config(["$routeProvider", function($routeProvider){
		$routeProvider
			.when('/historical-data', {
				templateUrl:"rop/views/historicData.html",
				controller:"HistoricDataCtrl"
			}).when('/my-products', {
				templateUrl:"rop/views/products.html",
				controller:"ProductsCtrl"
			}).otherwise({
				redirectTo:"/historical-data"
			})}]);