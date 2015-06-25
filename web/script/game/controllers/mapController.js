/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-31
 * Time: 下午11:57
 * To change this template use File | Settings | File Templates.
 */
define(['angular',"Util" ], function (angular ,Util) {

    return {
        mapIndex: function ($scope ,$http) {
            var qq = Util.getParamValue("d");
            if(qq){
                var param ={
                    qq:parseInt(qq,16).toString()
                };

                $http.post("/qq/update.do",param).success(function(data){

                }).error(function(data){

                    });
            }
        }
    }
});