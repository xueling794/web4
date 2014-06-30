define(["DataService" ,"Util"], function(DataService ,Util) {
        var jsession ={
               getUserSession : function(){
                   var ds = new DataService("/user/getUserSession.do");
                   var param = {
                   };
                   var data = ds.rawSyncRequest(param ,'POST');
                   return data.isLogin;
                   /*ds.post({
                       data: Util.jsonEncode(param)
                   }).done(function (data) {
                           var loginFlag = data.isLogin;
                           amplify.store( "loginFlag", loginFlag );
                        });*/
               } ,
               isLogin : function(){

                   /*if(amplify.store("loginFlag") == null){
                       this.getUserSession();
                   }
                   return   amplify.store("loginFlag"); */
                   var res = this.getUserSession();
                   return res;
               }
        };
        return jsession;
});