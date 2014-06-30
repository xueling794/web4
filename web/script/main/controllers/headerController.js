'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util","JSession"], function (angular, DataService, Util,jsession) {

    return {
        header: function ($scope) {
             //$scope.loginFlag=true;
            //controller模块

            /*$scope.getSession = function () {
                var ds = new DataService("user/getUserSession.do");
                var param = {
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        $scope.loginFlag = data.isLogin;
                    });

            };*/
            $scope.loginFlag =jsession.isLogin();
            $scope.doLogOut = function(){
                var ds = new DataService("/user/doLogOut.do");
                var param = {
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        var loginFlag = data.isLogin;
                        amplify.store( "loginFlag", loginFlag );
                        window.location.href = "/login.html#/login" ;

                    });
            };

            $scope.forwardUserBasic = function(){
                window.location.href = "/web/userSetting/index.html" ;
            };

            //$scope.getSession();
        }



    }

});
