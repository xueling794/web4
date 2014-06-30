'use strict';

/**
 * MyApp module
 */

define(['angular','main/controllers/activeResultController','validate',"main/controllers/headerController","Util","DataService","StateCode"],
    function(angular, activeResultController,validate ,headerController,Util,DataService,StateCode) {


        angular.module('activeResultIndex', [] ,function($compileProvider) {

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
                    templateUrl: '/script/main/template/activeResult.html',
                    replace: true,
                    transclude: false,
                    restrict: 'E',
                    controller : function ($scope,$http){
                       activeResultController.DoActive($scope,$http);
                    },
                    scope: true
                };
            });


        });



    });
