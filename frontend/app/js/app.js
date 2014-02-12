'use strict';

// IE console fix :
if (!window.console) window.console = {log: function() {}};


var doit = angular.module('doit', ['ngRoute', 'ui.bootstrap']);

doit.config(['$routeProvider', '$locationProvider',
	function($routeProvider, $locationProvider) {
		// Location provider config :
		$locationProvider.html5Mode(true);
		// Route provider config :
		$routeProvider
			.when('/', {
				templateUrl: '/partials/projects.html',
				controller: 'ProjectsCtrl'
			}).when('/projects/:id', {
				templateUrl: '/partials/project.html',
				controller: 'ProjectCtrl'
			}).when('/login', {
				templateUrl: '/partials/login.html',
				controller: 'LoginCtrl'
			}).otherwise({
				templateUrl: '/partials/404.html'
			});
	}
]);

doit.run(['$rootScope', '$location', 'AuthService',
	function($rootScope, $location, AuthService) {
		$rootScope.$on("$routeChangeStart", function(event, next, current) {
			// If we have no user, we authenticate
			if ($rootScope.user == null)
				AuthService.authenticate();
		});

		// Call when the 401 response is returned by the client
		$rootScope.$on('event:auth-loginRequired', function(rejection) {
			$rootScope.user = null;
			$location.path('/login').replace();
		});

		$rootScope.$on('event:auth-authConfirmed', function() {
			// If the login page has been requested and the user is already logged in
			// the user is redirected to the home page
			if ($location.path() === "/login")
				$location.path('/').replace();
		});
	}
]);