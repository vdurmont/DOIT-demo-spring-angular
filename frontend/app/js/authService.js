'use strict';

/* Authentication service */

doit.factory('AuthService', ['$rootScope', '$http',
	function ($rootScope, $http) {
		return {
			authenticate: function() {
				$http.get('/api/users/me').success(function(data) {
					$rootScope.user = data;
					$rootScope.$broadcast('event:auth-authConfirmed');
				}).error(function(err, status){
					console.log(status);
					$rootScope.$broadcast('event:auth-loginRequired');
				});
			}
		};
	}
]);