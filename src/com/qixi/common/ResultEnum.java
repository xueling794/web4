package com.qixi.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-1
 * Time: 上午12:25
 * To change this template use File | Settings | File Templates.
 */
public enum ResultEnum {
    FAILURE(			-1,	"失败"),
    SUCCESS(			0, 	"成功"),
    TOKEN_BAD(			2,  "The request token is invalid. "),
    TOKEN_EXPIRED(		3,  "The request token has expired. "),
    BAD_PARAMETER(		5,  "Incorrect request parameters. "),
    RECORD_EXISTS(		6,  "The record you are trying to create already exists. "),
    BAD_IDENTIFIER(		7,  "登陆已过期"),
    MISSING_PROPERTY(	8,  "Missing required property. "),
    BAD_PROPERTY(		9,  "Bad property value. "),
    OPERATION_FAILED(	10, "Operation failed. Please try again later. "),
    RECORD_DISABLED(	11, "The record has been disabled. "),
    DATA_NOT_FOUND(		12, "数据未找到"),
    BAD_RECORD(			13, "数据不正确"),
    VALIDATE_SUCCESS(	100, "验证成功"),
    SENSITIVE_WORD  (   101,"包含敏感词汇"),
    //code for group begin
    GROUP_INVALID  (   300,"群组不存在"),
    GROUP_NOT_MEMBER(   301,"非群组成员"),
    //code for group end
    INVALID_REQUEST(	-99, "无效的访问地址或请求"),
    INCORRECT_PASSWORD(	-100,"用户名或者密码错误"),
    USERNAME_INVALID(	-101,"用户名格式不对"),
    USERNAME_EXSIT(		-102,"用户名已经被注册"),
    USER_LOCKED(		-103,"用户已经被禁用"),
    USER_UNAUTHED(		-104,"用户尚未被激活"),
    EMAIL_INVALID(		-105,"邮箱地址输入错误"),
    EMAIL_EXSIT(		-106,"邮箱地址已经被注册"),
    EMAIL_NOEXSIT(		-107,"邮箱地址尚未注册"),
    MOBILE_INVALID(		-110,"手机号码输入错误"),
    MOBILE_EXSIT(		-111,"手机号码已经被注册"),
    CAPTCHA_ERROR( 		-112, "验证码错误"),
    USER_NEED_ACTIVE(	-115,"此用户需要激活"),
    SERVICE_EXCEPTION(	-999,"服务器内部错误"),
    UNKNOWN(			999, "Unknown error. "),
    BLACKLIST(			14, "您已在黑名单中");

    private int code;
    private String message;
    private static final Map<Integer, String> nameMap = new LinkedHashMap<Integer, String>();


    static
    {
        for (ResultEnum resultEnum : values()){
            nameMap.put(resultEnum.getCode(), resultEnum.getMessage());
        }
    }


    private ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMessagetByCode(int Code)
    {
        return nameMap.get(Code);
    }
}
