package com.qixi.db.entity;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-7
 * Time: 下午11:16
 * To change this template use File | Settings | File Templates.
 */
public class VoteCommentExtend extends VoteComment{
    private String nickName ;
    private String avatar;
    private Byte gender;

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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }
}
