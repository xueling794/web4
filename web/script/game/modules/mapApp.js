/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-31
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */

define(['angular', 'game/controllers/mapController','validate',"main/controllers/headerController"], function(angular, ctrls , validate ,headerController) {


    angular.module('mapApp', [] ,function($compileProvider) {
        $compileProvider.directive('header', function($compile) {
            return {
                templateUrl: '/script/main/template/header.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller : function ($scope){
                    //$scope.loginFlag =true;
                    headerController.header($scope);
                },
                scope: false
            };
        });
        $compileProvider.directive('footer', function($compile) {
            return {
                templateUrl: '/script/main/template/footer.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller : function ($scope){

                },
                scope: false
            };
        });

        $compileProvider.directive('container', function($compile) {
            return {
                templateUrl: '/script/game/template/map.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller :ctrls.mapIndex,
                scope: false
            };
        });

    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/', { templateUrl: '/script/game/template/map.html', controller: ctrls.mapIndex })

                .otherwise({redirectTo: '/'});
        }]);
});