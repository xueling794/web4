package com.qixi.common.Exception;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-22
 * Time: 下午9:50
 * To change this template use File | Settings | File Templates.
 */
public class BusinessException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -6798794546194003045L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(cause);
    }
}
