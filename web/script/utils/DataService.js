// brief: Request Format
// sample:
// GET:
// http://localhost:8080/11sns_api/a?service="modID"&method="methodName"&param={p1=1,p2="s"}
//
// POST: http://localhost:8080/11sns_api/a?service="modID"&method="methodName"
// {...}
// author:

define(["jquery"], function($) {
	var DataService = function(baseUrl) {
		this.baseUrl = baseUrl;

		this.advices = [];

		amplify.request.define(baseUrl + "get", "ajax", {
					url : baseUrl,
					dataType : "json",
					type : "GET"
				});

		amplify.request.define(baseUrl + "getWithCache", "ajax", {
					url : baseUrl,
					dataType : "json",
					type : "GET",
					cache : "persist"
				});

		amplify.request.define(baseUrl + "post", "ajax", {
					url : baseUrl,
					type : "POST",
					dataType : "json"
				});

		this.weaveAdvice(function(data) {
					var path = location.pathname;
					if ((data.resultCode == 7)
							&& !(path == "/" || /default\.html/.test(path))) {
						window.location = "/v/signin.htm?preUrl="
								+ (window.location.toString());
						return false;
					}
					return true;

				});

	};
	// simple handle script inject ,it alse must finally be handled in server
	var filterScript = function(param) {
		param
				&& param.data
				&& (typeof param.data == 'string')
				&& (param.data = param.data.replace(
						/(<script[^>]*>)(.*?)(<\/script>)/g, function($0, $1,
								$2, $3) {
							return $2;
						}));
	};
	DataService.prototype = {
		constructor : DataService,
		get : function(data) {
			filterScript(data);
			// inject cache bust
			(data || {}).cacheBust = new Date().getTime() + "";
			var obj = this;
			return $.Deferred(function(dfd) {
				amplify.request({
							resourceId : obj.baseUrl + "get",
							data : data,
							success : function() {
								if (obj.fireAdvice.apply(obj, arguments) === false) {
									console.log(JSON.stringify(data))
									return false;
								} else {
									return dfd.resolve.apply(null, arguments);
								}
							},
							error : dfd.reject
						});
			}).promise();
		},
		weaveAdvice : function(cb) {
			this.advices.push(cb);
		},
		fireAdvice : function() {
			var result = false;
			var self = this;
			var args = arguments;
			var len = this.advices.length;
			for (var index = 0; index < len; index++) {
				var flag = this.advices[index].apply(self, arguments);
				if (flag === false) {
					return flag;
				}
			}
			return true;
		},
		post : function(data) {
			filterScript(data);
			var obj = this;
			return $.Deferred(function(dfd) {
				amplify.request({
							resourceId : obj.baseUrl + "post",
							data : data,
							success : function() {
								if (obj.fireAdvice.apply(obj, arguments) === false) {
									console.log(JSON.stringify(data))
									return false;
								} else {
									return dfd.resolve.apply(null, arguments);
								}
							},
							error : dfd.reject
						});
			}).promise();
		},
		loadData : function() {

		},
		syncRequest : function(param, method, rawRequest) {
			filterScript(param);
			var obj = this;
			(param || {}).cacheBust = new Date().getTime() + "";
			var responseData;
			$.ajax(obj.baseUrl, {
						dataType : 'json',
						data : param,
						type : method || 'GET',
						async : false,
						success : function(data) {
							if (!rawRequest
									&& obj.fireAdvice.apply(obj, arguments) === false) {
								console.log(JSON.stringify(param))
								return false;
							} else {
								responseData = data;
							}

						}
					});
			return responseData;

		},
		rawSyncRequest : function(param, method) {
			filterScript(param);
			var obj = this;
			(param || {}).cacheBust = new Date().getTime() + "";
			var responseData;
			$.ajax(obj.baseUrl, {
						dataType : 'json',
						data : "data="+param,
						type : method || 'GET',
						async : false,
						success : function(data) {
							responseData = data;
						}
					});
			return responseData;

		}
	};

	return DataService;
});