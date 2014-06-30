'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode"], function (angular, DataService, Util, StateCode) {

    return {
        Login: function ($scope) {
            //css设置
            $('#headline').css("padding-top", 150);
            $('#loginBox').css("top", ($('#loginBtn').offset().top + $('#loginBtn')[0].offsetHeight + 20));
            $("div.login-arrow").css('margin-left', ($('#loginBtn').offset().left + $('#loginBtn')[0].offsetWidth / 2 - 11));
            $scope.testFlag = false;
            //controller模块

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
                            }else if(data.userBase.firstLoginFlag){
                                window.location.href = "/web/main/activation.html"
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
            //css设置
            $('#headline').css("padding-top", 150);
            $('#signupBox').css("top", ($('#regBtn').offset().top + $('#regBtn')[0].offsetHeight + 20));
            $("div.login-arrow").css('margin-left', ($('#regBtn').offset().left + $('#regBtn')[0].offsetWidth / 2 - 11));
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
                  console.log("forget");
            }
        },
        None: function ($scope) {
            //css设置
            $('#headline').css("padding-top", 170);
        }

    }

});
