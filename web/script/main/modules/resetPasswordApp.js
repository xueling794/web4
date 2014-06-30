'use strict';

/**
 * MyApp module
 */

define(['angular','main/controllers/resetPasswordController','validate',"main/controllers/headerController","Util","DataService","StateCode"],
    function(angular, resetPasswordController,validate ,headerController,Util,DataService,StateCode) {


        angular.module('resetPasswordApp', [] ,function($compileProvider) {

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
                    templateUrl: '/script/main/template/resetPassword.html',
                    replace: true,
                    transclude: false,
                    restrict: 'E',
                    controller : resetPasswordController.ResetPassword,
                    scope: true
                };
            });


        }).directive("password" , function(){
                return {
                    require:"ngModel",//NgModelController
                    link:function(scope,ele,attrs,ctrl) {
                        ctrl.$parsers.unshift(function (viewValue) {//$parsers，View到Model的更新
                            var result = validate.PASSWORD_CHECK(ctrl.$viewValue);
                            //console.log(scope.password);
                            if(result.resultType){
                                ctrl.$setValidity("newPassword", true);
                                return viewValue;
                            }else{
                                ctrl.$setValidity("newPassword", false);
                                return result.resultMsg;
                            }

                        });
                    }
                };
            });



    });
