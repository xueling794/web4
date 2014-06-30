/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-18
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */


'use strict';

/**
 * MyApp module
 */

define(['angular', '../controllers/loginAppController','validate'], function(angular, ctrls , validate) {


    angular.module('loginApp', [] ,function($compileProvider) {

    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/login', { templateUrl: 'script/main/template/login.html', controller: ctrls.Login })
                .when('/signup', { templateUrl: 'script/main/template/signup.html', controller: ctrls.Signup })
                .when('/forget', { templateUrl: 'script/main/template/forgetPassword.html', controller: ctrls.ForgetPassword })
                .otherwise({redirectTo: '/login'});
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
