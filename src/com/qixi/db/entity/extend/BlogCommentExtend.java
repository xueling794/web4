package com.qixi.db.entity.extend;

import com.qixi.db.entity.BlogComment;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-6
 * Time: 下午11:04
 * To change this template use File | Settings | File Templates.
 */
public class BlogCommentExtend extends BlogComment {
    private String nickName ;
    private String avatar;
    private byte gender;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }
}
