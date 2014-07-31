angular.module("locationsSelector",[])
	.controller("LocationsSelectorCtrl", ["$scope", function($scope){
		$scope.regions = [
			{id:1, name: "London"},
			{id:2, name: "Leeds"},
			{id:3, name: "Exeter"}
		]
	}])
	.directive("locationsSelector", function(){
		return {
			restrict:"E",
			replace:true,
			templateUrl:"rop/queryBar/locationsSelector/locationsSelector.html"
		}
	})