/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-18
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */


'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode"], function (angular, DataService, Util, StateCode) {

    return {
        Login: function ($scope) {
            //controller模块
            $scope.email ="dalianyg@126.com";
            $scope.password="123456";
            $scope.doLogin = function () {
                $("#loginBtn")[0].disabled = true;
                var ds = new DataService("user/validateUser.do");
                var param = {
                    "user": $scope.email,
                    "password": $scope.password
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        $("#loginBtn")[0].disabled = false;
                        $scope.test = "test";
                        if(data.validateResult){
                            if(!data.userBase.authFlag){
                                window.location.href = "/web/main/activation.html?email="+$scope.email;
                                return;
                            }else{
                                window.location.href = "/index.html";
                                return;
                            }
                        }else{
                            alert(data.validateResultMsg);
                        }

                    });

            };
        },

        Signup: function ($scope) {
           //初始化验证码


            $scope.doSignUp = function () {
                $("#signUpBtn")[0].disabled = true;
                var ds = new DataService("user/addNoAuthUser.do");
                var param = {
                    "user": $scope.email,
                    "password": $scope.password,
                    "randomKey"  : $scope.authCode
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        $("#signUpBtn")[0].disabled = false;
                        if(data.resultCode== StateCode.SUCCESS){
                            if(data.result){
                                window.location.href ="/web/main/activation.html?email="+$scope.email;;
                            }else{
                                alert(data.resultMsg);
                            }
                        }else{
                            alert(data.resultMessage);
                        }

                    });
            }
            $scope.getCaptcha = function () {
                var ds = new DataService("captcha/getCaptcha.do");
                var param = {
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        if (data.resultCode == StateCode.SUCCESS) {

                            $("#captchaImg").attr(
                                "src",
                                "data:image/jpeg;base64,"
                                    + data.captcha);

                        }

                    });
            }
            $scope.getCaptcha();
        },
        ForgetPassword: function ($scope) {
            $scope.doForget = function () {
                var ds = new DataService("email/sendPasswordEmail.do");
                var param = {
                    "email" : $scope.email
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        if(data.resultCode== StateCode.SUCCESS){
                            alert(data.sendResultMsg);
                        }else{
                            alert(data.resultMessage);
                        }
                    });
            }
        },
        None: function ($scope) {
            //css设置
            $('#headline').css("padding-top", 170);
        }

    }

});
