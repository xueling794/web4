'use strict';

/**
 * MyApp module
 */

define(['angular', 'main/controllers/activationController', 'validate',
    "main/controllers/headerController", "Util", "DataService", "StateCode"],
    function (angular, ctrls, validate, headerController, Util, DataService, StateCode) {


        angular.module('activationIndex', [], function ($compileProvider) {

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
                    templateUrl: '/script/main/template/activation.html',
                    replace: true,
                    transclude: false,
                    restrict: 'E',
                    controller: function ($scope) {
                        var userBaseService = new DataService("/user/getUserSession.do");

                        var param = {};
                        userBaseService.post({
                            data: Util.jsonEncode(param)
                        }).done(function (data) {
                                  $scope.userBase = data.userBase;
                                if(data.userBase != null){
                                    $scope.email = data.userBase.email
                                }
                            });

                        $scope.enterEmail = function () {
                            var email =  $scope.email || Util.getParamValue("email") ;
                            var webSite = Util.getWebSiteByEmail(email);
                            if (webSite != null) {
                                window.location.href = "http://" + webSite;
                            } else {
                                alert("您的邮箱地址无法访问，请你手动登陆邮箱");
                            }

                        };
                        $scope.sendActiveEmail = function () {

                            var email =   $scope.email || Util.getParamValue("email") ;
                            if (email == null || !validate.EMAIL_CHECK(email).resultType) {
                                alert("邮箱地址错误，请确保链接是有效的");
                            } else {
                                $("#reSendBtn")[0].disabled = true;
                                var ds = new DataService("/email/sendActiveEmail.do");
                                var param = {
                                    "email": email
                                };

                                ds.post({
                                    data: Util.jsonEncode(param)
                                }).done(function (data) {
                                        $("#reSendBtn")[0].disabled = false;
                                        if (data.resultCode == StateCode.SUCCESS) {
                                            if (data.sendResult) {
                                                alert("激活邮件重发成功,请您耐心等待");
                                            } else {
                                                alert(data.sendResultMsg);
                                            }
                                        } else {
                                            alert(data.resultMessage);
                                        }

                                    });
                            }

                        };
                    },
                    scope: false
                };
            });


        });


    });
