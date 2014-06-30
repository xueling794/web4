define([ 'text!../../web/main/footer.html', 'text!../../web/main/header.html',
		'text!./template/userBasic.html', 'app', 'routeConfig' ],
		function(footer, header, userBasic, app, routeConfig) {
	$('#footer').html(footer);
	$('#header').html(header);
	//$('#container').html(userBasic);
	var userBasic = angular.module("userBasic",[]);

	userBasic.directive("userinfo",function(){
		return{
			restrict : 'A',
			link : function(){
				alert("dasfsadf");
			}
		};
	});
	/*return app.config(function($routeProvider) {
		$routeProvider.when('userInfo', routeConfig.config(
				'userBasic/template/userInfo.html', 'userBasic/controller/userInfo'));
		$routeProvider.when('uploadIcon', routeConfig.config(
				'userBasic/template/uploadIcon.html', 'userBasic/controller/uploadIcon'));
		$routeProvider.when('password', routeConfig.config(
				'userBasic/template/userPassword.html', 'userBasic/controller/userPassword'));
		$routeProvider.otherwise({
			redirectTo : 'userInfo'
		});
	});

	return app;*/

});
