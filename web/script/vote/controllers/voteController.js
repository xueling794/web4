/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-4
 * Time: 下午10:51
 * To change this template use File | Settings | File Templates.
 */


'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode",'validate'], function (angular, DataService, Util, StateCode,validate) {

    return {
        voteQuery: function ($scope ,$http) {
            $("#voteMainTab li").removeClass();
            $("#queryTab").addClass('active');
            //获取用户信息

            /*$http.get('/user/getSelfUserInfo.do', { 'foo': 'bar' }).success(function(data){
                $scope.userBasic = data.userBasic;
                var date = new Date(data.userBasic.birthday)
                $scope.userBirth = Util.dateFormat(date,'yyyy-MM-dd');

            });
            //controller模块
            var startLmtDate = new Date();
            startLmtDate.setYear(1900);
            startLmtDate.setMonth(0);
            startLmtDate.setDate(1);
            $( "#datepicker" ).datepicker({
                format : 'yyyy-mm-dd',
                showOtherMonths: true,
                selectOtherMonths: false,
                autoclose: true,
                todayBtn: 'linked',
                endDate : new Date(),
                startDate : startLmtDate ,
                language : 'zh-CN'
            })*/
        },
        voteCreate: function ($scope ,$http) {
            $("#voteMainTab li").removeClass();
            $("#createTab").addClass('active');

            $( "#voteDatePicker" ).datepicker({
                format : 'yyyy-mm-dd',
                showOtherMonths: true,
                selectOtherMonths: false,
                autoclose: true,
                startDate : new Date(),
                language : 'zh-CN'
            })


            $scope.itemListArray = [
                {name:'备选项1',value:null},
                {name:'备选项2',value:null}
            ]

            $scope.selectArray = [
                {name:'单选' , value:1}
            ]
            $scope.selectNum = $scope.selectArray[0];

            $scope.addItem = function(){
                var length = $scope.itemListArray.length;
                if(length<10){
                    length = length +1;
                    var item  = {name:'备选项'+length , value:null}
                    $scope.itemListArray.push(item);
                    var select = {name:'可多选,最多'+(length-1)+'项',value : (length-1)};
                    $scope.selectArray.push(select);
                }

            }

            $scope.removeItem = function(){
                var length = $scope.itemListArray.length;
                var selLength = $scope.selectArray.length;
                if(length>2){
                   $scope.itemListArray = $scope.itemListArray.slice(0,length-1);
                    $scope.selectArray = $scope.selectArray.slice(0,selLength-1);
                }
            }
        }

    }

});
