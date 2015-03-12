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
        blogQuery: function ($scope ,$http) {
            $scope.pageNumber = 1;
            $scope.pageSize = 10;
            $scope.commentEnd = true;
            $scope.commentList = [];
            $scope.comment = {};
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
            $scope.blog = {};
            $scope.blogId = Util.getParamValue("blogId");
            $http.get('/blog/'+$scope.blogId+'/getBlog.do', { data: Util.jsonEncode({})}).success(function(data){
                if(data.resultCode == StateCode.SUCCESS){
                    data.blog.createDate = new Date(data.blog.createDate);
                    $scope.blog = data.blog;
                    $("#contentDiv")[0].innerHTML = data.blog.content;
                }else{
                    alert("获取话题信息失败");
                }


            });
            $scope.getBlogCommentList = function(){
                var param ={
                    start : ($scope.pageNumber-1)*$scope.pageSize,
                    size : $scope.pageSize
                };
                $http.post('/blog/'+$scope.blogId+'/getBlogComment.do?data='+Util.jsonEncode(param), { data: Util.jsonEncode(param)}).success(function(data){
                    if(data.resultCode == StateCode.SUCCESS){
                        if(data.blogCommentList == null || data.blogCommentList.length==0){
                            $scope.commentEnd = true;
                            return ;
                        }
                        if(data.blogCommentList.length <$scope.pageSize){
                            $scope.commentEnd = true;
                        }else{
                            $scope.commentEnd = false;
                        }
                        if(data.blogCommentList != null && data.blogCommentList.length>0){
                            for(var i=0 ,j=data.blogCommentList.length; i<j ; i++){
                                data.blogCommentList[i].createDate = new Date(data.blogCommentList[i].createDate);

                            }
                            $scope.commentList = data.blogCommentList;
                        }
                    }else{
                        alert("获取话题评论信息失败");
                    }


                });
            };
            $scope.nextPageBlogComment = function(){
                $scope.pageNumber = $scope.pageNumber +1 ;
                $scope.getBlogCommentList();
            } ;
            $scope.prePageBlogComment = function(){
                $scope.pageNumber = $scope.pageNumber -1 ;
                if($scope.pageNumber >= 1){
                    $scope.getBlogCommentList();
                }
            };


            $scope.addComment= function (){
                var param ={
                    blogId : $scope.blogId,
                    authCode : $scope.authCode,
                    content : $scope.comment.content
                };
                $http.post("/blog/"+$scope.blogId+"/createBlogComment.do",param).success(function(data){
                    $scope.getCaptcha();
                    if(data.resultCode == StateCode.SUCCESS){
                        alert("发表评论成功");

                        $scope.commentList.unshift(data.result);
                        $("#commentForm")[0].reset();

                    }else{
                        alert(data.resultMessage) ;
                    }
                }).error(function(data){
                        $scope.getCaptcha();
                        alert(data.resultMessage) ;
                    });
            } ;
            $scope.getCaptcha();
            $scope.getBlogCommentList();
        }
    }

});