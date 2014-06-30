var userBasic = angular.module("userBasic",[]);

userBasic.directive("userinfo",function(){
	return{
		restrict : 'A',
		link : function(){
			alert("dasfsadf");
		}
	};
});