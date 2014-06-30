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
                $scope.birthday = Util.dateFormat(date,'yyyy-MM-dd');
            });
            /*var ds = new DataService("/user/getSelfUserInfo.do");
            var param = {
            };

            ds.post({
                data: Util.jsonEncode(param)
            }).done(function (data) {
                    console.log(data);
                    if(data.resultCode == StateCode.SUCCESS){
                        $scope.userBasic = data.userBasic;
                    }else{
                        alert(data.resultMessage);
                    }
                });*/
            //controller模块
            var startLmtDate = new Date();
            startLmtDate.setYear(1990);
            startLmtDate.setMonth(0);
            startLmtDate.setDate(1);
            $( "#datepicker" ).datepicker({
                showOtherMonths: true,
                selectOtherMonths: false,
                autoclose: true,
                todayBtn: 'linked',
                endDate : new Date(),
                startDate : startLmtDate,
                language : 'zh'
            });
           /* $('#birthDatePicker').datepicker({
                autoclose: true,
                todayBtn: 'linked',
                endDate : new Date(),
                startDate : startLmtDate
            }).on('changeDate', function(ev){
                var selDate = ev.date;
                var constellation = $scope.getConstellation(selDate.getFullYear(),selDate.getMonth()+1,selDate.getDate());
                var constellationStr = $scope.getAstro(constellation);
                $scope.contellationImg = "/img/constellation/"+constellationStr+".png";
                $scope.contellationName = constellation;
            } );*/

            $scope.userBirth ="1984-12-12";

            $scope.saveBaseInfo = function(){
                  console.log($("#birthTxt").val());
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
