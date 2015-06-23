package com.qixi.common;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-6-22
 * Time: 下午9:12
 * To change this template use File | Settings | File Templates.
 */
public class EmailConfig {

    private String account ;
    private String accountPswd ;
    private String mailServer ;
    private String mailServerPort ;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountPswd() {
        return accountPswd;
    }

    public void setAccountPswd(String accountPswd) {
        this.accountPswd = accountPswd;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }
}
