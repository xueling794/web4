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
        ResetPassword: function ($scope,$http) {
            //controller模块
            $scope.newPassword = "";
            $scope.authCode = "";
            $scope.resetFlag = false;
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

                var param = {
                    "randomKey": $scope.authCode,
                    "encodeData": Util.getParamValue('data'),
                    "newPassword":$scope.newPassword
                };
                $http.post("/user/resetUserPassword.do",param).success(function(data){
                    $scope.authCode = "";
                    $scope.newPassword = "";
                    $scope.getCaptcha();
                    $("#resetBtn")[0].disabled = false;
                    if(data.resultCode == StateCode.SUCCESS){
                        $scope.resetFlag = true;
                        alert("密码重置成功，请记住信息密码");
                    }else{
                        alert(data.resultMessage);
                    }
                }).error(function(data){
                        $scope.getCaptcha();
                        alert(data.resultMessage) ;
                    });


            };
        }

    };

});