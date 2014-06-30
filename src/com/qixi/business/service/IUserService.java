package com.qixi.business.service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.UserBasic;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-7
 * Time: 下午11:35
 * To change this template use File | Settings | File Templates.
 */
public interface IUserService {
    public ResultInfoEntity validateUser(String user, String password) throws BusinessException;

    public int addNewUser(String email, String password, String randomKey) throws BusinessException;

    public ResultInfoEntity changePassword(String email, String originPassword, String newPassword) throws BusinessException;

    public ResultInfoEntity addNoAuthUser(String email, String password, String randomKey) throws BusinessException;

    public UserBasic getUserBasicByUid(int uid) throws BusinessException;

    public UserBasic getUserBasicByUuid(String uuid) throws BusinessException;

    public UserBasic getUserBasicByEmail(String email) throws BusinessException;

    public boolean setUserAuth(String email) throws BusinessException;

    public ResultInfoEntity setUserAuthByUrl(String data) throws BusinessException;

    public ResultInfoEntity updateUserBase(UserBasic userBasic)throws BusinessException;

    public ResultInfoEntity resetUserPassword(String data ,String newPassword,String randomKey) throws  BusinessException;
}
