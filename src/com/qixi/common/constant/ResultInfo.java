package com.qixi.common.constant;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-2-11
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public class ResultInfo {
    public static final String REG_NO_USER = "用户尚未注册";

    public static final String REG_USER_NULL = "请输入有效的邮箱、密码以及验证码";

    public static final String REG_EMAIL_ERROR = "请输入正确格式的邮箱地址";

    public static final String REG_CAPTCHA_ERROR = "请输入正确的验证码";

    public static final String REG_USER_EXIST = "该邮箱地址已经被注册";

    public static final String REG_SERVICE_ERROR = "注册新用户失败";

    public static final String LOGIN_USER_NULL = "请输入有效的邮箱地址和密码";

    public static final String LOGIN_USER_NO_EXIST = "该邮箱地址尚未注册";

    public static final String LOGIN_USER_PASSWORD_ERROR = "登陆密码输入错误";

    public static final String LOGIN_SERVICE_ERROR = "用户登陆失败";

    public static final String EMAIL_ACTIVE_SEND_SUCCESS =  "激活邮件发送成功";
    public static final String EMAIL_ACTIVE_SEND_FAILURE =  "激活邮件发送失败";

    public static final String EMAIL_PSWD_SEND_SUCCESS = "重置密码邮件发送成功";
    public static final String EMAIL_PSWD_SEND_FAILURE = "重置密码有发送失败";

    public static final String USER_DO_ACTIVE_AGAIN =  "账户已经被激活";

    public static final String USERBASIC_UPDATE_FAILURE =  "用户基本信息保存失败";

    public static final String USER_NO_LOGIN_ERROR =  "登陆超时，无法完成此操作";

    public static final String USER_RESET_PSWD_ERROR = "重置密码失败";
    public static final String USER_RESET_PSWD_SUCCESS = "重置密码成功";

    public static final String USER_CHANGE_PSWD_ERROR = "修改密码失败"  ;
    public static final String USER_CHANGE_PSWD_SUCCESS = "修改密码成功"  ;
    public static final String USER_CHANGE_PSWD_CHECK = "原始密码错误"  ;

}
