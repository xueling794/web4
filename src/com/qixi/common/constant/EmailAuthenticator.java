package com.qixi.common.constant;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-4
 * Time: 下午11:10
 * To change this template use File | Settings | File Templates.
 */
public class EmailAuthenticator extends Authenticator {
    String userName=null;
    String password=null;


    public EmailAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }
}
