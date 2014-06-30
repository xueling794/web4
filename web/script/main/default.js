define([ 'text!../../web/main/footer.html', 'text!../../web/main/login.html','text!../../web/main/signup.html'],
		function(footer,login,signup) {
	$('#footer').html(footer);
	$('#loginBox').html(login);
	$('#signupBox').html(signup);
	$('#captchaRefresh').bind("click",function(){
		alert("刷新");
	});
	$('h1.demo-logo').bind("click",function(){
		hideAll();
	});
	
	$('#loginBtn').bind("click",function(){
		$('#headline').css("padding-top",150);
		$('#signupBox').css("display","none");	
		if($('#loginBox').css('display') == 'none'){
			showLogin();
		}else{
			hideAll();
		}	
	});

	$('#regBtn').bind("click",function(){
		
		
		$('#loginBox').css("display","none");
		if($('#signupBox').css('display') == 'none'){
			showSignup();
		}else{
			hideAll();
		}	
	});
	
	var hashStr = window.location.hash;
	if(hashStr.indexOf('signup')>0){
		showSignup();
	}
	if(hashStr.indexOf('login')>0){
		showLogin();
	}
	function showLogin(){
		$('#headline').css("padding-top",150);
		$('#loginBox').css("display","block");
		$('#loginBox').css("top",($('#loginBtn').offset().top+$('#loginBtn')[0].offsetHeight+20));
		$("div.login-arrow").css('margin-left',($('#loginBtn').offset().left +$('#loginBtn')[0].offsetWidth/2-11));
	};
	
	function hideLogin(){
		
	};
	
	function showSignup(){
		$('#headline').css("padding-top",150);
		$('#signupBox').css("display","block");
		$('#signupBox').css("top",($('#regBtn').offset().top+$('#regBtn')[0].offsetHeight+20));
		$("div.login-arrow").css('margin-left',($('#regBtn').offset().left +$('#regBtn')[0].offsetWidth/2-11));
	};
	
	function hideSignup(){
		
	};
	
	function hideAll(){
		$('#headline').css("padding-top",170);
		$('#signupBox').css("display","none");	
		$('#loginBox').css("display","none");	
	}
});
