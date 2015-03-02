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
         create : function($scope){

             var dropZoneForm = new dropZone("form#my-dropzone", { url: "/image/upload.do"});
             dropZoneForm.on("complete", function(file,a) {
                 console.log(file);
             });
             dropZoneForm.options.myAwesomeDropzone  = {
                 maxFilesize: 2,
                 accept: function(file, done) {
                     if (file.name == "justinbieber.jpg") {
                         done("Naha, you don't.");
                     }
                     else { done(); }
                 },
                 init: function() {
                     this.on("addedfile", function(file) {
                         alert("Added file.");
                     });
                     /*this.on("addedfile", function(file) {
                         // Create the remove button
                         var removeButton = Dropzone.createElement("<button class='btn btn-sm btn-block'>Remove file</button>");

                         // Capture the Dropzone instance as closure.
                         var _this = this;

                         // Listen to the click event
                         removeButton.addEventListener("click", function(e) {
                             // Make sure the button click doesn't submit the form:
                             e.preventDefault();
                             e.stopPropagation();

                             // Remove the file preview.
                             _this.removeFile(file);
                             // If you want to the delete the file on the server as well,
                             // you can do the AJAX request here.
                         });

                         // Add the button to the file preview element.
                         file.previewElement.appendChild(removeButton);
                     });*/
                 }
             }   ;
             //dropZoneForm.init();
             KindEditor.create('textarea[name="content"]', {
                     resizeType : 1,
                     allowPreviewEmoticons : true,
                     uploadJson : '/uploadImg' ,
                     allowImageUpload : false,
                     items : [
                         'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                         'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                         'insertunorderedlist', '|', 'image','|', 'preview']
                 });

         }
    }

});