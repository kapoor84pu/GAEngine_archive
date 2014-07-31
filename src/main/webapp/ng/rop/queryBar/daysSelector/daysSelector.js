angular.module("daysSelector",["ui.bootstrap"])
	.controller("DaysSelectorCtrl", ["$scope", function($scope){
	}])
	.directive("daysSelector",function(){
		return {
			restrict:"E",
			templateUrl:"rop/queryBar/daysSelector/daysSelector.html",
			replace: true,
			scope:{
				days:'='
			}
		}
	})