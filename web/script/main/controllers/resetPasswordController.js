/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-11
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */


'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode"], function (angular, DataService, Util, StateCode) {

    return {
        ResetPassword: function ($scope) {
            //controller模块
            $scope.newPassword = "";
            $scope.authCode = "";
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

            $scope.resetPassword = function () {
                if(Util.getParamValue('data') == null){
                    alert("参数错误,请确认链接是否有效");
                    return;
                }
                $("#resetBtn")[0].disabled = true;
                var ds = new DataService("user/resetUserPassword.do");
                var param = {
                    "randomKey": $scope.authCode,
                    "encodeData": Util.getParamValue('data'),
                    "newPassword":$scope.newPassword
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                    $("#resetBtn")[0].disabled = false;
                        if(data.resultCode == StateCode.SUCCESS){
                            alert(data.resultMessage);
                        }else{
                            alert(data.resultMessage);
                        }


                });

            };
        }

    };

});