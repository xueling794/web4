'use strict';

/**
 * Controllers
 */

define(["angular","DataService", "Util", "StateCode","validate"], function(angular, DataService, Util, StateCode,validate) {

    return {
        Index: function($scope , $http) {
            $scope.pageNumber = 1;
            $scope.pageSize = 10;
            $scope.blogEnd = true;
            $scope.blogList = [];

            $scope.getBlogList = function(){
                var param ={
                    start : ($scope.pageNumber-1)*$scope.pageSize,
                    size : $scope.pageSize
                };
                $http.get('/blog/getBlogList.do?data='+Util.jsonEncode(param), { data: Util.jsonEncode(param)}).success(function(data){
                    if(data.resultCode == StateCode.SUCCESS){
                        if(data.blogList == null || data.blogList.length==0){
                            //$scope.voteCommentList = null;
                            $scope.blogEnd = true;
                            return ;
                        }
                        if(data.blogList.length <$scope.pageSize){
                            $scope.blogEnd = true;
                        }else{
                            $scope.blogEnd = false;
                        }
                        if(data.blogList != null && data.blogList.length>0){
                            for(var i=0 ,j=data.blogList.length; i<j ; i++){
                                data.blogList[i].createDate = new Date(data.blogList[i].createDate);
                                data.blogList[i].commentDate = new Date(data.blogList[i].commentDate);

                            }
                            $scope.blogList = data.blogList;
                        }

                    }else{
                        alert(data.resultMessage) ;
                    }


                });
            }
            $scope.nextPageBlog = function(){
                $scope.pageNumber = $scope.pageNumber +1 ;
                $scope.getBlogList();
            } ;
            $scope.prePageBlog = function(){
                $scope.pageNumber = $scope.pageNumber -1 ;
                if($scope.pageNumber >= 1){
                    $scope.getBlogList();
                }
            };

            $scope.getBlogList();

        },

        Create: function($scope, $location, Project) {
            $scope.legend = 'New User';
            $scope.save = function() {
                Project.save($scope.user, function(user) {
                    $location.path('/' + user._id.$oid);
                });
            }
        }

    }

});
