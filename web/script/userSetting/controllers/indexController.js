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
                var date = new Date(data.userBasic.birthday)
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


        UploadAvatar: function ($scope) {
            $("#settingTab li").removeClass();
            $("#avatarTab").addClass('active');
            $.fn.editable.defaults.mode = 'inline';
            $.fn.editableform.loading = "<div class='editableform-loading'><i class='light-blue icon-2x icon- fa fa-spinner fa-spin'></i></div>";
            $.fn.editableform.buttons = '<button type="submit" class="btn btn-info editable-submit"><i class="icon- fa fa-check"></i></button>'+
                '<button type="button" class="btn editable-cancel"><i class=" fa fa-times"></i></button>';
            $('#username').on('init', function(e, editable) {
                var colors = {0: "gray", 1: "green", 2: "blue", 3: "red"};
                $(this).css("color", colors[editable.value]);
            });
            $('#username').editable({

                tpl: '<span><input type="hidden" /></span><span><input type="file" /></span>',
                inputclass: '',
                image:
                {
                    style: 'well',
                    btn_choose: 'Change Image',
                    btn_change: null,
                    no_icon: 'icon- fa fa-picture-o',
                    thumbnail: 'large'
                }
            });
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
