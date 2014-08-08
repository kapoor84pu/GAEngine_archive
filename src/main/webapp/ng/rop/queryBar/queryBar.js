angular.module("queryBar",["datesRangeSelector", "locationsSelector", "obsService"])
	.controller("QueryBarCtrl",["$scope", "$http", "$filter", "obsService", function($scope, $http, $filter, obsService){
		var ONE_WEEK =  1000 * 60 * 60 * 24 * 7;
		var NOW = (new Date()).getTime();
		$scope.dates = {from: new Date(NOW), to: new Date(NOW + ONE_WEEK)};

		$scope.days = {
			monday:true,
			tuesday:true,
			wednesday:true,
			thursday:true,
			friday:true,
			saturday:false,
			sunday:false
		};

		$scope.regions =  [
			{id:1, name: "London"},
			{id:2, name: "Leeds"},
			{id:3, name: "Exeter"}
		];

		$scope.submit = function(){
			obsService.getObs($scope.parsedData());
		};

		$scope.parsedData = function(){
			var selectedDays = [];
			var selectedRegions = [];
			for (var day in $scope.days){
				if($scope.days.hasOwnProperty(day) && $scope.days[day]){
					selectedDays.push(day);
				}
			}
			for (var i=0; i<$scope.regions.length; i++){
				var region = $scope.regions[i];
				if(region.selected){
					selectedRegions.push(region);
				}
			}

			return {
				from : $scope.dates.from,
				to : $scope.dates.to,
				days : selectedDays,
				regions : selectedRegions
			}
		}



	}])
	.directive("queryBar", function(){
		return {
			templateUrl:"rop/queryBar/queryBar.html",
			restrict:"E",
			replace: true,
			scope:{}
		}
	});