'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode"], function (angular, DataService, Util, StateCode) {

    return {
        Profile: function ($scope ,$http) {
            var uid = Util.getParamValue("uid");
            var param = {
                uid : parseInt(uid)
            }
            //$http.get('/user/getProfile.do?data='+Util.jsonEncode(param) , { data: Util.jsonEncode(param)}).success(function(data){
            $http.post('/user/getProfile.do', param).success(function(data){
                data.userBasic.signUpTime =  Util.dateFormat( new Date(data.userBasic.signUpTime),'yyyy-MM-dd hh:mm:ss');
                data.userBasic.lastLoginTime =  Util.dateFormat( new Date(data.userBasic.lastLoginTime),'yyyy-MM-dd hh:mm:ss');
                $scope.userBasic = data.userBasic;
                var date = new Date(data.userBasic.birthday)
                $scope.userBirth = Util.dateFormat(date,'yyyy-MM-dd');
            });

        }
    }
});
