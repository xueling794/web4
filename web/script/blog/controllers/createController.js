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


define(['angular', "DataService", "Util", "StateCode",'validate','DropZone'], function (angular, DataService, Util, StateCode,validate,dropZone) {

    return {
         create : function($scope , $http){
             $scope.imageList = [];
             $scope.textContent  = "";

             $scope.title = "";
             $scope.kindEditor = null;
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
                             $("#captchaComplexImg").attr(
                                 "src",
                                 "data:image/jpeg;base64,"
                                     + data.captcha);

                         }

                     });
             }
             $scope.getCaptcha();

             $scope.addHtmlComment = function(){
                 var content = $scope.kindEditor.html();
                 var param ={
                     title : $scope.title,
                     authCode : $scope.authCode,
                     content : content
                 };
                 $http.post("/blog/createBlog.do",param).success(function(data){
                     $scope.getCaptcha();
                     if(data.resultCode == StateCode.SUCCESS){
                         alert("发表评论成功");

                         window.href = "/index.html" ;

                     }else{
                         alert(data.resultMessage) ;
                     }
                 }).error(function(data){
                         $scope.getCaptcha();
                         alert(data.resultMessage) ;
                     });
             }

             $scope.addComment = function(){
                 var content = "";
                 if($scope.imageList || $scope.imageList.length >0){
                     for(var i =0 ,j= $scope.imageList.length; i<j ; i++){
                         content = content + '<div class="rows"><img src="/image/'+$scope.imageList[i]+'._png" style="max-width: 100%;"/></div>';
                     }
                 }
                 content =  content  +'<div class="rows"><span style="font-size:20px;">'+ $scope.textContent +"</span></div>";
                 var param ={
                     title : $scope.title,
                     authCode : $scope.authCode,
                     content : content
                 };
                 $http.post("/blog/createBlog.do",param).success(function(data){
                     $scope.getCaptcha();
                     if(data.resultCode == StateCode.SUCCESS){
                         alert("发表评论成功");
                         window.href = "/index.html" ;

                     }else{
                         alert(data.resultMessage) ;
                     }
                 }).error(function(data){
                         $scope.getCaptcha();
                         alert(data.resultMessage) ;
                     });
             }
             var dropZoneForm = new dropZone("form#my-dropzone", { url: "/image/upload.do"});
             dropZoneForm.on("complete", function(file,a) {
                 var uploadResponse = eval("("+file.xhr.response+")");
                 if(uploadResponse.resultCode == StateCode.SUCCESS){
                     $scope.imageList = $scope.imageList.concat(uploadResponse.imageUrl) ;
                 } else{
                     alert(uploadResponse.resultMessage);
                 }

             });
             dropZoneForm.options.myAwesomeDropzone  = {
                 maxFilesize: 2,
                 accept: function(file, done) {

                 },
                 init: function() {
                     this.on("addedfile", function(file) {
                         alert("Added file.");
                     });
                 }
             }   ;
             //dropZoneForm.init();
            $scope.showEditor = function(){
                $scope.kindEditor = KindEditor.create($("#contentId"), {
                    resizeType : 1,
                    allowPreviewEmoticons : true,
                    uploadJson : '/uploadImg' ,
                    allowImageUpload : false,
                    items : [
                        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                        'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                        'insertunorderedlist', '|', 'image','|', 'source','|', 'preview']
                });

            }

         }
    }

});