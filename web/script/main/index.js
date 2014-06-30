define([ 'text!../../web/main/footer.html','text!../../web/main/header.html'
         ,'text!../../web/main/navigator.html'],
		function(footer,header,navigator) {
	$('#header').html(header);
	$('#footer').html(footer);
	$('#naviContainer').html(navigator);
	
});
