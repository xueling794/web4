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
            $scope.pageNumber = 1;
            $scope.pageSize = 10;
            $scope.commentEnd = true;
            $scope.voteMulSelect =[];
            $scope.voteCommentList = [];
            $(window).bind("scroll",function() {
                if ($(document).scrollTop() + $(window).height() > $(document).height() - 80) {
                    if(!$scope.commentEnd){
                        $scope.pageNumber += 1;
                        $scope.getComment();
                    }
                }
            });
            var voteId = Util.getParamValue("res");
            var param ={
                voteId : parseInt(voteId)
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
                    var nowDateLong = new Date().getTime();
                    if(data.voteExtend.endDate.getTime()<nowDateLong){
                        data.voteExtend.expired = true;
                    }else{
                        data.voteExtend.expired = false;
                    }
                    $scope.voteDetail = data;
                    $scope.voteSelect = data.voteExtend.voteItemList[0].id ;
                    $scope.voteMulSelect.push($scope.voteSelect);
                    data.voteSelectFlag = data.voteSelectFlag == null ? false: data.voteSelectFlag;

                    $scope.showChart= data.voteSelectFlag;
                    if($scope.showChart){
                        data.voteSelectList[0].createDate = new Date(data.voteSelectList[0].createDate);
                        data.voteSelectStr = $scope.getVoteSelectStr(data.voteSelectList,data.voteExtend.voteItemList);
                        $scope.showVoteChart(data.voteResult);
                    }
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
                        if(data.resultCode == StateCode.SUCCESS){
                            alert("投票成功");
                            $scope.showChart(data.voteResult);

                        }else{
                            alert(data.resultMessage) ;
                        }
                    });
            }

            $scope.addComment= function (){
                var param ={
                    voteId : $scope.voteDetail.voteExtend.id,
                    authCode : $scope.authCode,
                    comment : $scope.commentInfo.comment
                };
                $http.post("/vote/addVoteComment.do",param).success(function(data){
                    $scope.getCaptcha();
                    if(data.resultCode == StateCode.SUCCESS){
                        alert("发表评论成功");
                        $("#commentForm")[0].reset();
                        $scope.voteCommentList.unshift(data.voteCommentExtend);

                    }else{
                        alert(data.resultMessage) ;
                    }
                }).error(function(data){
                    $scope.getCaptcha();
                    alert(data.resultMessage) ;
                });
            } ;

            $scope.getComment = function(){
                var param ={
                    voteId : parseInt(voteId),
                    start : ($scope.pageNumber-1)*$scope.pageSize,
                    size : $scope.pageSize
                };

                $http.get('/vote/getVoteComment.do?data='+Util.jsonEncode(param), { data: Util.jsonEncode(param)}).success(function(data){
                    if(data.resultCode == StateCode.SUCCESS){
                        if(data.voteCommentList == null || data.voteCommentList.length==0){
                            //$scope.voteCommentList = null;
                            $scope.commentEnd = true;
                            return ;
                        }
                        if(data.voteCommentList.length <$scope.pageSize){
                            $scope.commentEnd = true;
                        }else{
                            $scope.commentEnd = false;
                        }
                        for(var i=0;i<data.voteCommentList.length;i++){
                            data.voteCommentList[i].createTime = new Date(data.voteCommentList[i].createTime);

                        }
                        $scope.voteCommentList = $scope.voteCommentList.concat(data.voteCommentList);


                    }else{
                        alert(data.resultMessage) ;
                    }

                });
                /*var ds = new DataService("/vote/getVoteComment.do");
                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {

                    });*/
            }

            $scope.getVoteSelectStr = function(voteSelectList ,voteItemList){
                var voteSelectStrArray = [];
                for(var i= 0; i<voteItemList.length;i++){
                    for(var j=0;j<voteSelectList.length ; j++){
                        if(voteItemList[i].id == voteSelectList[j].id){
                            voteSelectStrArray.push(voteItemList[i].connent) ;
                            break;
                        }
                    }
                }
                return voteSelectStrArray.join(',');
            }
            $scope.getCaptcha = function () {
                var ds = new DataService("/captcha/getCaptcha.do");
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

            $scope.getVoteResult = function(){
                var param ={
                    voteId : $scope.voteDetail.voteExtend.id
                };
                $http.post("/vote/getVoteResult.do",param).success(function(data){
                    if(data.resultCode == StateCode.SUCCESS){

                        $scope.showVoteChart(data.voteResult);

                    }else{
                        alert(data.resultMessage) ;
                    }
                }).error(function(data){
                        alert(data.resultMessage) ;
                    });
            }
            $scope.showVoteChart = function(voteResult){
                $scope.showChart = true;
                var dataArray = [];
                var labelArray = [];
                for(var i=0 ,j=voteResult.length ; i<j ; i++){
                    dataArray.push(voteResult[i].itemSelectCount);
                    labelArray.push(voteResult[i].connent);
                }
                $('#voteChartDiv').highcharts({
                    chart: {
                        type: 'bar' ,
                        height :60*voteResult.length ,
                        maxheight: 400
                    },
                    title:{
                        text:null
                    },
                    xAxis: {
                        categories: labelArray,
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
                        data: dataArray
                    }]
                });

            }

            $scope.getCaptcha();
            $scope.getComment();
        }

    }

});

