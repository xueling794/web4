/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-31
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
define(['angular', 'game/controllers/2048Controller','validate',"main/controllers/headerController"], function(angular, ctrls , validate ,headerController) {


    angular.module('2048App', [] ,function($compileProvider) {
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

        /*$compileProvider.directive('container', function($compile) {
            return {
                templateUrl: '/script/game/template/2048.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller :ctrls.increaseDetail,
                scope: false
            };
        });*/

    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/', { templateUrl: '/script/game/template/2048.html', controller: ctrls.increaseDetail })

                .otherwise({redirectTo: '/'});
        }]);
});