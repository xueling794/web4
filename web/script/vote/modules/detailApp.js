/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-17
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-4
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */


define(['angular', 'vote/controllers/detailController','validate',"main/controllers/headerController"], function(angular, ctrls , validate ,headerController) {


    angular.module('detailApp', [] ,function($compileProvider) {
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
                templateUrl: '/script/vote/template/detail.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller :ctrls.voteDetail,
                scope: false
            };
        });

    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/', { templateUrl: '/script/vote/template/detail.html', controller: ctrls.voteDetail })
                .when('/vote/:voteId', { templateUrl: '/script/vote/template/detail.html', controller: ctrls.voteDetail })
               .otherwise({redirectTo: '/'});
        }]);



});
