package com.qixi.common;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-1
 * Time: 上午12:23
 * To change this template use File | Settings | File Templates.
 */
public class UserBase {

    private int id;

    private String uuid;

    private String nickName;

    private String email;

    private String iconUrl;

    private int state;

    private boolean authFlag;

    private boolean firstLoginFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFirstLoginFlag() {
        return firstLoginFlag;
    }

    public void setFirstLoginFlag(boolean firstLoginFlag) {
        this.firstLoginFlag = firstLoginFlag;
    }

    public boolean isAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(boolean authFlag) {
        this.authFlag = authFlag;
    }
}
