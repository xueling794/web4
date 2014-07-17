/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-17
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */

'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode",'validate'], function (angular, DataService, Util, StateCode,validate) {

    return {
        voteDetail: function ($scope ,$http,$routeParams) {
             $scope.title ="详细信息";

        }

    }

});

