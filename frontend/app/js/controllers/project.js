'use strict';

/* Controllers */

angular.module('doit').controller('ProjectCtrl', ['$scope', '$http', '$routeParams',
	function($scope, $http, $routeParams) {

		$http.get("/api/projects/"+$routeParams.id).success(function(data) {
			// TODO fake data
			data.name = data.name + " Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco aboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
			data.stats = {
				numMembers: 2,
				numTasks: 42
			};

			$scope.project = data;
		}).error(function(data){
			// TODO handle error
			console.log("error..");
		});
	}
]);

