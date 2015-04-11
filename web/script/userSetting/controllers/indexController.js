'use strict';

/**
 * Controllers
 */

define(['angular', "DataService", "Util", "StateCode",'validate'], function (angular, DataService, Util, StateCode,validate) {

    return {
        BaseInfo: function ($scope ,$http) {
            $("#settingTab li").removeClass();
            $("#baseTab").addClass('active');
            //获取用户信息

            $http.get('/user/getSelfUserInfo.do', { 'foo': 'bar' }).success(function(data){
                $scope.userBasic = data.userBasic;
                var date = new Date(data.userBasic.birthday ||'1970-1-1')
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
            })




            $scope.saveBaseInfo = function(){
                $("#baseInfoBtn")[0].disabled = true;
                var param ={
                    nickName : $scope.userBasic.nickName,
                    signature : $scope.userBasic.signature ,
                    gender : $scope.userBasic.gender,
                    birthday : $('#datepicker').val()
                } ;

                var ds = new DataService("/user/updateUser.do");
                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                       $("#baseInfoBtn")[0].disabled = false;
                        if(data.resultCode == StateCode.SUCCESS){
                            if(data.result){
                                alert("修改个人信息成功");
                            }else{
                                alert(data.resultMsg);
                            }
                        }else{
                            alert(data.resultMessage) ;
                        }
                    });
            } ;
            $scope.getConstellation = function(curYear,curMonth,curDay){

                var s="魔羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
                var arr=[20,19,21,21,21,22,23,23,23,23,22,22];
                var constellation = s.substr(curMonth*2-(curDay<arr[curMonth-1]?2:0),2);
                return constellation;
            };
            $scope.getAstro = function(astro){
                if(astro=="魔羯")
                    return "mojie";
                if(astro=="水瓶")
                    return "shuiping";
                if(astro=="双鱼")
                    return "shuangyu";
                if(astro=="白羊")
                    return "baiyang";
                if(astro=="金牛")
                    return "jinniu";
                if(astro=="双子")
                    return "shuangzi";
                if(astro=="巨蟹")
                    return "juxie";
                if(astro=="狮子")
                    return "shizi";
                if(astro=="处女")
                    return "chunv";
                if(astro=="天秤")
                    return "tiancheng";
                if(astro=="天蝎")
                    return "tianxie";
                if(astro=="射手")
                    return "sheshou";

            };
            $scope.doLogin = function () {

            };
        },


        UploadAvatar: function ($scope,$http) {
            $("#settingTab li").removeClass();
            $("#avatarTab").addClass('active');

            var input = document.getElementById("img_input");
            var temp_img =document.getElementById('temp_img');
            var display_img =document.getElementById('display_img');
            var preview_img =document.getElementById('preview_img');
            var jcropApi = null;
            var boundx, boundy;
            var anchor = null;
            var preDataURL = null;
            input.addEventListener( 'change',readFile,false );
            function readFile(){
                temp_img.src = null;
                display_img.src = null;

                var file = this.files[0];

                if(jcropApi != null){
                    jcropApi.destroy();
                }

                if(!/image\/\w+/.test(file.type)){
                    alert("Type error");
                    return false;
                }
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    console.log(e);
                    temp_img.src = this.result;
                    var w = temp_img.width, h = temp_img.height;

                    h = parseInt(display_img.width/w * h);
                    w = display_img.width;


                    var dataURL
                    try {
                        var canvas = document.createElement('canvas');
                        canvas.width = w; canvas.height = h;
                        var context = canvas.getContext('2d');
                        context.drawImage(temp_img, 0, 0, temp_img.width, temp_img.height, 0, 0, w, h);
                        dataURL = canvas.toDataURL(file.type);
                        display_img.src = dataURL;
                        //preview_img.src =dataURL;
                        $("#display_img").css("height",h);
                        $("#temp_img").css("display","none");
                        $('#display_img').Jcrop({
                            onChange: updatePreview,
                            onSelect: updatePreview,
                            bgFade:     true,
                            bgOpacity: .2,
                            setSelect: [ 10, 10, 190, 190 ],
                            allowSelect:false,
                            minSize: [ 80, 80 ],
                            aspectRatio: 1
                        },function(){
                            var bounds = this.getBounds();
                            boundx = bounds[0];
                            boundy = bounds[1];
                            jcropApi = this;
                            //$preview.appendTo(jcropApi.ui.holder);
                        });

                        console.log(dataURL.length/1024);
                    } catch(e) {
                        dataURL = null;
                    }
                    console.log(dataURL);
                    /*result.innerHTML = '<img src="'+this.result+'" alt=""/>';
                     img_area.innerHTML = '<div class="sitetip">img explore:</div><img src="'+this.result+'" alt=""/>';*/
                }
            }
            function updatePreview(c)
            {
                anchor =c;
                var previewWidth = $("#preview-pane").width();
                var canvas = document.createElement('canvas');
                canvas.width = previewWidth; canvas.height = previewWidth;
                var context = canvas.getContext('2d');
                context.drawImage(display_img, c.x, c.y,c.w, c.h,0,0,previewWidth,previewWidth);
                preDataURL = canvas.toDataURL();
                preview_img.src = preDataURL;
            };
            $scope.uploadAvatar = function(){

                var param ={
                    imageData : preDataURL
                };
                $http.post("/user/setAvatar.do",param).success(function(data){
                    if(data.resultCode == StateCode.SUCCESS){
                        alert("个人头像上传成功");


                    }else{
                        alert(data.resultMessage) ;
                    }
                }).error(function(data){
                        alert(data.resultMessage) ;
                    });
                /*var canvas = document.createElement('canvas');

                temp_img.width= display_img.width*100/anchor.w;
                temp_img.height = display_img.height*100/anchor.h;
                canvas.width = temp_img.width; canvas.height = temp_img.height;
                console.log(temp_img.width);
                var context = canvas.getContext('2d');
                context.drawImage(display_img, 0, 0,display_img.width,display_img.height,0,0,temp_img.width,temp_img.height);
                var tempData = canvas.toDataURL();
                console.log(tempData.length) ;
                temp_img.src =tempData;*/
            }
        },
        ChangePassword: function ($scope , $http) {
            $("#settingTab li").removeClass();
            $("#passwordTab").addClass('active');
                console.log("init Password");
            $scope.changePassword = function(){
                var newPassword = $scope.newPassword;
                var confirmPassword = $scope.confirmPassword;
                if(newPassword != confirmPassword){
                    alert(validate.PWD_NOTSAME_ERROR)  ;
                    return;
                }
                $("#changePswdBtn")[0].disabled = true
                var param  ={
                    password : $scope.originPassword,
                    newPassword : newPassword
                }

                ;
                var ds = new DataService("/user/changePassword.do");


                ds.post({
                    data: Util.jsonEncode(param)
                }).done(function (data) {
                        $("#changePswdBtn")[0].disabled = false;
                        if(data.resultCode == StateCode.SUCCESS){
                             if(data.result){
                                 alert("修改密码成功");
                             }else{
                                 alert(data.resultMsg);
                             }
                        }else{
                            alert(data.resultMessage) ;
                        }

                    });

            }
        }

    }

});
