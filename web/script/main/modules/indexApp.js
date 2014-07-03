'use strict';

/**
 * MyApp module
 */

define(['angular', 'main/controllers/indexAppController','validate',"main/controllers/headerController"], function(angular, ctrls , validate ,headerController) {


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
                }
                ,
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
                templateUrl: '/script/main/template/index.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller : ctrls.Index,
                scope: true
            };
        });


    });



});
