package com.qixi.common.constant;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-2-11
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public class ResultInfo {
    /**
     * This module for system
     */
    public static final String SYS_INTERNAL_ERROR = "服务器内部错误";
    /**
     * The model for user
     */
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

    public static final String USER_AVATAR_IMAGE_ERROR = "图片格式错误"  ;

    /**
     * The model for vote
     */

    public static final String VOTE_ITEM_SIZE_LESS = "选项不能够少于"+VoteConst.VOTE_ITEM_MIN+"项";
    public static final String VOTE_ITEM_SIZE_MORE = "选项不能够少于"+VoteConst.VOTE_ITEM_MAX+"项";
    public static final String VOTE_MAX_ITEM_ERROR = "可投选择数错误";
    public static final String VOTE_TITLE_EMPTY_ERROR = "投票主题未正确填写";
    public static final String VOTE_REMARK_EMPTY_ERROR = "投票详细说明为正确填写";
    public static final String VOTE_ADD_EXCEPTION = "创建投票失败";
    public static final String VOTE_ADD_ITEM_ERROR = "添加投票选项失败";
    public static final String VOTE_UPDATE_ITEM_ERROR = "修改投票选项失败";
    public static final String VOTE_ADD_SELECT_ERROR = "添加投票失败";
    public static final String VOTE_ADD_SELECT_DUPLICATE = "不能重复提交投票";
    public static final String VOTE_ADD_COMMENT_ERROR = "添加投票评论失败";
    public static final String VOTE_USER_SELECT_ERROR = "本次投票失败";
    public static final String VOTE_GET_VOTE_ERROR = "获取投票信息失败";

    /**
     * This module for blog
     */


    public static final String BLOG_TITLE_EMPTY_ERROR = "话题标题未正确填写";
    public static final String BLOG_CONTENT_EMPTY_ERROR = "话题内容未正确填写";
    public static final String BLOG_COMMENT_CONTENT_EMPTY_ERROR = "话题评论内容未正确填写";
    public static final String BLOG_ADD_EXCEPTION = "创建话题失败";
    public static final String BLOG_ADD_COMMENT_ERROR = "添加评论失败";
    public static final String BLOG_GET_BLOG_ERROR = "获取话题信息失败";
    public static final String BLOG_GET_BLOG_COMMENT_ERROR = "获取话题评论信息失败";

}
