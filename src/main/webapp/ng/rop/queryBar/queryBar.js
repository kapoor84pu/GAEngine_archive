angular.module("queryBar",["datesRangeSelector"])
	.controller("QueryBarCtrl",["$scope", function($scope){
		var ONE_WEEK =  1000 * 60 * 60 * 24 * 7;
		var NOW = (new Date()).getTime();
		$scope.dates = {text:"hey", from: new Date(NOW), to: new Date(NOW + ONE_WEEK)};
		$scope.newDate = function(){
			$scope.dates.to = new Date($scope.dates.to.getTime() + 1000 * 60 * 60 * 24 * 7);
			$scope.dates.from = new Date($scope.dates.from.getTime() - 1000 * 60 * 60 * 24 * 7);
		}
	}])
	.directive("queryBar", function(){
		return {
			templateUrl:"rop/queryBar/queryBar.html",
			restrict:"E",
			replace: true
		}
	})