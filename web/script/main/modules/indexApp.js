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


    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/login', { templateUrl: '/script/main/template/login.html', controller: ctrls.Login })
                .when('/signup', { templateUrl: '/script/main/template/signup.html', controller: ctrls.Signup })
                .otherwise({template:'<div></div>' , controller: ctrls.None  });
        }]).directive("password" , function(){
            return {
                require:"ngModel",//NgModelController
                link:function(scope,ele,attrs,ctrl) {
                    ctrl.$parsers.unshift(function (viewValue) {//$parsers，View到Model的更新
                        var result = validate.PASSWORD_CHECK(viewValue);
                        //console.log(scope.password);
                        if(result.resultType){
                            ctrl.$setValidity("password", true);
                            return viewValue;
                        }else{
                            ctrl.$setValidity("password", false);
                            return result.resultMsg;
                        }

                    });
                }
            };
        });



});
