define([], function () {
    var validate;
    var passwordMinLength = 6;
    var passwordMaxLength  =16;
    validate = {

        EMAIL_NULL_ERROR: "邮箱不能为空",
        EMAIL_INVALID_ERROR: "请输入格式正确的邮箱",
        EMAIL_DUPLICATE_ERROR: "该邮箱已被注册",

        PHONE_NULL_ERROR: "手机号码不能为空",
        PHONE_INVALID_ERROR: "请输入格式正确的手机号码",
        PHONE_DUPLICATE_ERROR: "该手机号码已被注册",

        PWD_LEN_MAX: passwordMaxLength,
        PWD_LEN_MIN: passwordMinLength,
        PWD_NULL_ERROR: "请输入密码",
        PWD_MIN_ERROR: "请输入" + passwordMinLength + "位以上密码",
        PWD_MAX_ERROR: "密码长度不要超过" + passwordMaxLength + "位",
        PWD_NOTSAME_ERROR: "两次输入的密码不一致",

        CAPTCHA_MSG_ERROR: "验证码错误",

        USER_NICKNAME_NOTNULL: "用户的昵称不能为空",
        USER_NICKNAME_OVERLOAD: "用户昵称不应超过20个字",

        PASSWORD_REGEX: "",
        EMAIL_REGEX: "/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/",
        PHONE_REGEX: "/^([0-9]{11})?$/",

        SAME_PASSWORD_CHECK : function(newPassword , confirmPassword){
              if(newPassword != confirmPassword){
                  return {
                      resultType: false,
                      resultMsg: this.PWD_NOTMATCH_ERROR
                  };
              }else{
                  return {
                      resultType: true,
                      resultMsg: ""
                  }
              }
        },
        PASSWORD_CHECK: function (password) {
            if (password == null || password == "") {
                return {
                    resultType: false,
                    resultMsg: this.PWD_NULL_ERROR
                };
            }
            if (password.length < this.PWD_LEN_MIN) {
                return {
                    resultType: false,
                    resultMsg: this.PWD_MIN_ERROR
                };
            } else if (password.length > this.PWD_LEN_MAX) {
                return {
                    resultType: false,
                    resultMsg: this.PWD_MAX_ERROR
                };
            } else {
                return {
                    resultType: true,
                    resultMsg: ""
                };
            }
        },
        EMAIL_CHECK: function (email) {
            if (email == null || email == "") {
                return {
                    resultType: false,
                    resultMsg: this.EMAIL_NULL_ERROR
                };
            }
            if (email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1) {
                return {
                    resultType: true,
                    resultMsg: ""
                };
            } else {
                return {
                    resultType: false,
                    resultMsg: this.EMAIL_INVALID_ERROR
                };
            }
        },
        PHONE_CHECK: function (phone) {
            if (phone == null || phone == "") {
                return  {
                    resultType: false,
                    resultMsg: this.EMAIL_NULL_ERROR
                };
            }
            if (phone.search(/^([0-9]{11})?$/) != -1) {
                return  {
                    resultType: true,
                    resultMsg: ""
                };
            } else {
                return   {
                    resultType: false,
                    resultMsg: this.PHONE_INVALID_ERROR
                };
            }
        }

    };
    return validate;
});