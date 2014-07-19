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

            $scope.voteMulSelect =[];
            var param ={
                voteId : 1
            }
            $scope.toggleSelection = function(itemId,domId){

                var idx = $scope.voteMulSelect.indexOf(itemId);
                if (idx > -1) {
                    $scope.voteMulSelect.splice(idx, 1);
                }else {
                    $scope.voteMulSelect.push(itemId);
                }
                if($scope.voteMulSelect.length>$scope.voteDetail.voteExtend.maxItem){
                    $scope.voteMulSelect.splice($scope.voteMulSelect.length-1, 1);
                    $('#'+domId)[0].checked = false;
                    alert("本次投票最多可以选择"+$scope.voteDetail.voteExtend.maxItem+"选项");
                    return;
                }

            }
            $http.get('/vote/getVoteDetail.do?data='+Util.jsonEncode(param), { data: Util.jsonEncode(param)}).success(function(data){
                if(data.resultCode == StateCode.SUCCESS){
                    data.voteExtend.createDate = new Date(data.voteExtend.createDate);
                    data.voteExtend.endDate = new Date(data.voteExtend.endDate);

                    /*if(data.voteList != null && data.voteList.length>0){
                     for(var i=0 ,j=data.voteList.length; i<j ; i++){
                     data.voteList[i].createDate = new Date(data.voteList[i].createDate);
                     data.voteList[i].endDate = new Date(data.voteList[i].endDate);
                     }
                     }*/
                    $scope.voteDetail = data;
                    $scope.voteSelect = data.voteExtend.voteItemList[0].id ;
                    $scope.voteMulSelect.push($scope.voteSelect);
                    $scope.showChart= true;
                    $scope.showVoteChart();

                }else{
                    alert("获取投票信息失败");
                }


            });
            $scope.doSelect = function(){
                var itemIdArray = [];
                if($scope.voteDetail.voteExtend.maxItem>1){
                   itemIdArray = $scope.voteMulSelect;
                }else{
                    itemIdArray.push($scope.voteSelect);
                }
                if(itemIdArray == null || itemIdArray.length<1){
                    alert("请先选择至少一个选项");
                    return;
                }
                var param ={
                    voteId : $scope.voteDetail.voteExtend.id,
                    itemIdArray :itemIdArray
                } ;
                var ds = new DataService("/vote/addUserVoteSelect.do");
                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        console.log(data);
                    });
            }

            $scope.showVoteChart = function(){
                $('#voteChartDiv').highcharts({
                    chart: {
                        type: 'bar'
                    },
                    title:{
                        text:null
                    },
                    xAxis: {
                        categories: ['Africadsafsdfsafsewrasfasfrear', 'America'],
                        title: {
                            text: null
                        },

                        gridLineWidth : 0 ,
                        showEmpty :false ,
                        allowDecimals : false
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: '',
                            align: 'high'
                        },
                        labels: {
                            rotation: 45,
                            enabled : false
                        } ,
                        gridLineWidth : 0
                    },

                    plotOptions: {
                        bar: {

                        }
                    },

                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: "投票数",
                        data: [3, 1]
                    }]
                });

            }


        }

    }

});

