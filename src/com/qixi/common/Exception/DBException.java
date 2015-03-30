package com.qixi.common.Exception;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-22
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */
public class DBException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = -6826632103908500434L;

    public DBException() {
        super("DB Exception");
    }

    public DBException(String message) {
        super("DB Exception : " + message);
    }

    public DBException(Throwable cause) {
        super("DB Exception", cause);
    }

    public DBException(String message, Throwable cause) {
        super("DB Exception : " + message, cause);
    }
}
