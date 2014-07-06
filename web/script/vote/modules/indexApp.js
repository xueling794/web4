/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-4
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */


define(['angular', 'vote/controllers/voteController','validate',"main/controllers/headerController"], function(angular, ctrls , validate ,headerController) {


    angular.module('indexApp', [] ,function($compileProvider) {

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
                templateUrl: '/script/vote/template/voteMain.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller : function ($scope){
                    $scope.showQueryView = function(){
                        $("#voteMainTab li").removeClass();
                        $("#queryTab").addClass('active');
                    };

                    $scope.showCreateView = function(){
                        $("#voteMainTab li").removeClass();
                        $("#createTab").addClass('active');
                    };

                },
                scope: false
            };
        });
    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/query', { templateUrl: '/script/vote/template/query.html', controller: ctrls.voteQuery })
                .when('/create', { templateUrl: '/script/vote/template/create.html', controller: ctrls.voteCreate })
                .otherwise({redirectTo: '/query'});
        }]);



});