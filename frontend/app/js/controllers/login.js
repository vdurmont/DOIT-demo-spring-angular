'use strict';

angular.module('doit').controller('LoginCtrl', ['$scope', '$http', '$rootScope',
	function($scope, $http, $rootScope) {
		$scope.login = function() {
			$http({
				method: 'POST',
				url: "/api/login",
				data: "email="+$scope.email+"&password="+$scope.password,
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			}).success(function(data){
				$rootScope.$broadcast("event:auth-authConfirmed");
			}).error(function(data, status) {
				console.log("error: "+status);
				console.dir(data);
			});
		};

		$scope.register = function() {
			console.log("Register with email="+$scope.email+" , password="+$scope.password);
		};
	}
]);

