package com.qixi.business.model;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-2-11
 * Time: 下午11:04
 * To change this template use File | Settings | File Templates.
 */
public class ResultInfoEntity {


    private boolean resultFlag ;

    private String resultInfo ;

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public boolean isResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(boolean resultFlag) {
        this.resultFlag = resultFlag;
    }

}
