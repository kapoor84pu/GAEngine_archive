app.controller("personController", function ($scope) {
	$scope.person = {first:"John", last:"Doe", age:30,
		revName:function(){
			return $scope.person.last + ", " + $scope.person.first;
		},
		incAge:function(){
			$scope.person.age++;
		}
	};

	$scope.features = [
		"Hook nose",
		"Long hair",
		"Tall"
	];

	$scope.whoAmI = function(){
		return "You are '" + $scope.person.revName() + "'";
	}
});



