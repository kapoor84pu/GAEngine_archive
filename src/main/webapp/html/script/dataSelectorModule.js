var dataSelectorModule = angular.module('dataSelectorModule', []);



dataSelectorModule.controller("DataSelectorController", ['$scope', function($scope){
	$scope.data = {date:"Today"}
}]);

dataSelectorModule.directive("moDataSelector", function(){
	return {
		restrict: "E",
		templateUrl: "script/dataSelectorTemplate.html"
	}
});


