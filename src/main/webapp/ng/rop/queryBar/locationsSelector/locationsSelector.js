angular.module("locationsSelector",[])
	.controller("LocationsSelectorCtrl", ["$scope", function($scope){

	}])
	.directive("locationsSelector", function(){
		return {
			restrict:"E",
			replace:true,
			templateUrl:"rop/queryBar/locationsSelector/locationsSelector.html",
			scope:{
				regions:'='
			}
		};
	})