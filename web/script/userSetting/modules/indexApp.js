'use strict';

/**
 * MyApp module
 */

define(['angular', 'userSetting/controllers/indexController','validate',"main/controllers/headerController"], function(angular, ctrls , validate ,headerController) {


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
                templateUrl: '/script/userSetting/template/userSettingMain.html',
                replace: true,
                transclude: false,
                restrict: 'E',
                controller : function ($scope){
                    $scope.showBaseView = function(){
                        $("#settingTab li").removeClass();
                        $("#baseTab").addClass('active');
                    };

                    $scope.showAvatarView = function(){
                        $("#settingTab li").removeClass();
                        $("#avatarTab").addClass('active');
                    };

                    $scope.showPasswordView = function(){
                        $("#settingTab li").removeClass();
                        $("#passwordTab").addClass('active');
                    };

                },
                scope: false
            };
        });
    }).config(['$routeProvider', function($routeProvider) {
            $routeProvider
                .when('/baseInfo', { templateUrl: '/script/userSetting/template/baseInfo.html', controller: ctrls.BaseInfo })
                .when('/uploadAvatar', { templateUrl: '/script/userSetting/template/uploadAvatar.html', controller: ctrls.UploadAvatar })
                .when('/changePassword', { templateUrl: '/script/userSetting/template/changePassword.html', controller: ctrls.ChangePassword })
                .otherwise({redirectTo: '/baseInfo'});
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
