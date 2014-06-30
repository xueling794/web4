'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode"], function (angular, DataService, Util, StateCode) {

    return {
        DoActive: function ($scope ,$http) {
            $scope.resultFlag = true;
            $scope.resultMsg ="激活成功";
            $scope.testFlag = false;

            $scope.enterIndex = function(){
                window.location = "/index.html";
            };


            var data = Util.getParamValue("data");
            if(data ==null){
                $scope.resultMsg ="链接失效,激活失败";
                $scope.resultFlag = false;

            }else{
                var ds = new DataService("/user/activeUser.do");
                var param = {
                    "data": data
                };

                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        if(data.resultCode == StateCode.SUCCESS){
                            if(data.activeResult){
                                $scope.resultFlag = true;
                                $scope.resultMsg = "账户 "+data.activeResultMsg+" 激活成功";
                            }else{
                                $scope.resultFlag = false;
                                $scope.resultMsg = data.activeResultMsg;
                            }
                        }else{
                            $scope.resultFlag = false;
                            $scope.resultMsg = data.resultMessage;
                        }
                    });
               /* var param = {
                    "data": data
                };
                $http({
                    method : "post",
                    url : "/user/activeUser.do",
                    data : $.param({"data":Util.jsonEncode(param)})

                }).success(function(data){
                    if(data.resultCode == StateCode.SUCCESS){
                        if(data.activeResult){
                            $scope.resultFlag = true;
                            $scope.resultMsg = "账户 "+data.activeResultMsg+" 激活成功";
                        }else{
                            $scope.resultFlag = false;
                            $scope.resultMsg = data.activeResultMsg;
                        }
                    }else{
                        $scope.resultFlag = false;
                        $scope.resultMsg = data.resultMessage;
                    }
                });*/

                }
        }
    }
});
