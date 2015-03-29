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
                 if(!$scope.validateComplex()){
                     return;
                 }
                 var content = $scope.kindEditor.html();
                 var param ={
                     title : $scope.title,
                     authCode : $scope.authCode,
                     content : content
                 };

                 $http.post("/blog/createBlog.do",param).success(function(data){
                     $scope.getCaptcha();
                     if(data.resultCode == StateCode.SUCCESS){
                         window.location.href = "/web/blog/detail.html?blogId="+data.blogId ;
                     }else{
                         alert(data.resultMessage) ;
                     }
                 }).error(function(data){
                         $scope.getCaptcha();
                         alert(data.resultMessage) ;
                     });
             }

             $scope.addComment = function(){
                 if(!$scope.validateSimple()){
                       return;
                 }
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
                         window.location.href = "/web/blog/detail.html?blogId="+data.blogId ;

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

            $scope.validateSimple = function(){
                if($scope.title == null || $scope.title.length<5 || $scope.title.length>50){
                    alert("标题字数长度在5-50之间") ;
                    return false;
                }
                if($scope.textContent == null || $scope.textContent.length<20) {
                    alert("内容字数不能少于20字") ;
                    return false;
                }
                if($scope.authCode == null || $scope.authCode.length != 5)  {
                    alert("请输入有效的验证码") ;
                    return false;
                }
                  return true;
            }
            $scope.validateComplex = function(){
                if($scope.title == null || $scope.title.length<5 || $scope.title.length>50){
                    alert("标题字数长度在5-50之间") ;
                    return false;
                }
                var content = $scope.kindEditor.html();
                if(content == null || content.length<20) {
                    alert("内容字数不能少于20字") ;
                    return false;
                }
                if($scope.authCode == null || $scope.authCode.length != 5)  {
                    alert("请输入有效的验证码") ;
                    return false;
                }
                return true;
            }

         }
    }

});