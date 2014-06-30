package com.qixi.common.Exception;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-22
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */
public class DBException extends Exception{

    public DBException() {
        super("DAO Exception");
    }

    public DBException(String message) {
        super("DAO Exception : " + message);
    }

    public DBException(Throwable cause) {
        super("DAO Exception", cause);
    }

    public DBException(String message, Throwable cause) {
        super("DAO Exception : " + message, cause);
    }
}
