package com.qixi.business.service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.BusinessException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-4
 * Time: 下午10:23
 * To change this template use File | Settings | File Templates.
 */
public interface IEmailService {
    public boolean sendEmail(String email, String title, String content) throws BusinessException;

    ResultInfoEntity sendActiveEmail(String email ,String data) throws BusinessException;

    ResultInfoEntity sendPasswordEmail(String email,String userName ,String data) throws BusinessException;
}
