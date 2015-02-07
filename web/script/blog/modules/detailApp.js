'use strict';

/**
 * MyApp module
 */

define(['angular', 'blog/controllers/detailController', 'validate',
    "main/controllers/headerController", "Util", "DataService", "StateCode"],
    function (angular, ctrls, validate, headerController, Util, DataService, StateCode) {


        angular.module('detailApp', [], function ($compileProvider) {

            $compileProvider.directive('header', function ($compile) {
                return {
                    templateUrl: '/script/main/template/header.html',
                    replace: true,
                    transclude: false,
                    restrict: 'E',
                    controller: function ($scope) {
                        //$scope.loginFlag =true;
                        headerController.header($scope);
                    },
                    scope: false
                };
            });
            $compileProvider.directive('footer', function ($compile) {
                return {
                    templateUrl: '/script/main/template/footer.html',
                    replace: true,
                    transclude: false,
                    restrict: 'E',
                    controller: function ($scope) {

                    },
                    scope: false
                };
            });
            $compileProvider.directive('container', function ($compile) {
                return {
                    templateUrl: '/script/blog/template/detail.html',
                    replace: true,
                    transclude: false,
                    restrict: 'E',
                    controller: function ($scope) {

                    },
                    scope: false
                };
            });


        });


    });
