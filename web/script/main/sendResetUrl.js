define([ 'text!../../web/main/footer.html','text!../../web/main/header.html'],
		function(footer,header) {
	$('#header').html(header);
	$('#footer').html(footer);
	
	$('#sendResetBtn').bind('click',function(){
		alert("close");
	});
});
