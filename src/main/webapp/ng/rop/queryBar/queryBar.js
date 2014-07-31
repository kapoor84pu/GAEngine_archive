angular.module("queryBar",["datesRangeSelector", "locationsSelector"])
	.controller("QueryBarCtrl",["$scope", function($scope){
		var ONE_WEEK =  1000 * 60 * 60 * 24 * 7;
		var NOW = (new Date()).getTime();
		$scope.dates = {text:"hey", from: new Date(NOW), to: new Date(NOW + ONE_WEEK)};

		$scope.days = {
			monday:true,
			tuesday:true,
			wednesday:true,
			thursday:true,
			friday:true,
			saturday:false,
			sunday:false
		}

	$scope.regions =  [
		{id:1, name: "London"},
		{id:2, name: "Leeds"},
		{id:3, name: "Exeter"}
	]

	}])
	.directive("queryBar", function(){
		return {
			templateUrl:"rop/queryBar/queryBar.html",
			restrict:"E",
			replace: true
		}
	})