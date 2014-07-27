'use strict';
 var requireConfig ={
    baseUrl: '/script',
    paths: {
        text:'lib/text',
        jquery:'lib/jquery-2.0.3.min',
        angular:'lib/angular.min',
        angularResource: 'lib/angular-resource.min' ,
        validate : 'utils/validate',
        amplify : 'lib/amplify.min',
        DataService : 'utils/DataService',
        Util : 'utils/Util' ,
        StateCode : 'utils/StateCode',
        JSession : 'utils/JSession'
    },
    shim: {
        jquery: {
            exports: 'jquery'
        },
        angular: {
            deps: ['jquery'],
            exports: 'angular'
        },
        angularResource: {
            deps: ['angular'],
            exports: 'angularResource'
        }
    }
};

window.alert = function (txt) {
    var _myAlert = document.createElement("div");
    var id = "_alert"+ parseInt(100*Math.random());
    _myAlert.id = id;
    _myAlert.setAttribute("class","alert alert-info");
    _myAlert.style.textAlign = "center";
    _myAlert.style.zIndex = "10000" ;
    _myAlert.style.width = "100%" ;
    _myAlert.style.position = "fixed" ;
    _myAlert.style.backgroundColor = "orange" ;
    //_myAlert.style.position = "relative" ;
    var _myAlertBtn = document.createElement("button");
    _myAlertBtn.type ="button" ;
    _myAlertBtn.innerHTML = '<i class="fa fa-times"></i>';
    _myAlertBtn.setAttribute("class","close");
    _myAlertBtn.setAttribute("data-dismiss","alert");
   // _myAlertBtn.setAttribute("aria-hidden","true");

    var _myAlertTxt = document.createElement("strong");
    _myAlertTxt.innerText = txt;
    _myAlertTxt.style.width = "100%";
    _myAlertTxt.style.textAlign = "center";

    _myAlert.appendChild(_myAlertBtn);
    _myAlert.appendChild(_myAlertTxt);
    var s = document.getElementsByTagName("div")[0];
    window.top.document.body.insertBefore(_myAlert,s);
    setTimeout(function() {

        $("#" + id).slideDown();
        var _alertDom = document.getElementById(id);
        if(_alertDom != null){
            window.top.document.body.removeChild(_alertDom);
        }

    }, 5000);



};

