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
            var param ={
                start :0 ,
                size :10
            }
            $http.get('/vote/getActiveVote.do?data='+Util.jsonEncode(param), { data: Util.jsonEncode(param)}).success(function(data){
               if(data.resultCode == StateCode.SUCCESS){
                   if(data.voteList != null && data.voteList.length>0){
                       for(var i=0 ,j=data.voteList.length; i<j ; i++){
                           data.voteList[i].createDate = new Date(data.voteList[i].createDate);
                           data.voteList[i].endDate = new Date(data.voteList[i].endDate);
                       }
                   }
                   $scope.voteList = data.voteList;
               }else{
                   alert("获取投票信息失败");
               }


            });
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

            $scope.createVote = function(){
                var itemArray = []
                for(var i= 0, j=$scope.itemListArray.length; i<j; i++){
                    itemArray.push($scope.itemListArray[i].value);
                }
                var param ={
                    title : $scope.voteTitle,
                    remark : $scope.voteDesc ,
                    endDate : $('#voteDatePicker').val(),
                    maxItem : $scope.selectNum.value,
                    itemArray :itemArray
                } ;
                var ds = new DataService("/vote/createVote.do");
                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                       console.log(data);
                    });

            }
        }

    }

});
