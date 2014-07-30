


angular.module("datesRangeSelector",["ui.bootstrap"])
	.controller("DateRangeCtrl", ["$scope", function($scope){
		$scope.showToDateSelect = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.toDatePopupOpened = true;
		};

		$scope.showFromDateSelect = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.fromDatePopupOpened = true;
		};
	}])


	.directive("dateRangeSelector", function(){
		return {
			templateUrl:"rop/queryBar/datesRangeSelector/datesRangeSelector.html",
			restrict:"E",
			replace: true,
			scope:{
				dates:'='
			}
		}
	})
