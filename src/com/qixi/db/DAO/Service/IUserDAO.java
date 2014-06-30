package com.qixi.db.DAO.Service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.UserBasic;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-7
 * Time: 下午11:39
 * To change this template use File | Settings | File Templates.
 */
public interface IUserDAO {

    /**
     * 验证用户与密码是否一致
     * @param user
     * @param password
     * @return
     * @throws BusinessException
     */
    public ResultInfoEntity validateUser(String user, String password) throws BusinessException;

    /**
     *添加一个已经激活的用户
     * @param email
     * @param password
     * @param randomKey
     * @return
     * @throws BusinessException
     */
    int addNewUser(String email, String password, String randomKey) throws BusinessException;

    /**
     * 用户用新密码替换旧密码
     * @param email
     * @param originPassword
     * @param newPassword
     * @return
     * @throws BusinessException
     */
    ResultInfoEntity changePassword(String email, String originPassword, String newPassword) throws BusinessException;

    /**
     * 添加一个为激活的用户
     * @param email
     * @param password
     * @param randomKey
     * @return
     * @throws BusinessException
     */
    ResultInfoEntity addNoAuthUser(String email, String password, String randomKey) throws BusinessException;

    UserBasic getUserBasicByUid(int uid) throws BusinessException;

    UserBasic getUserBasicByUuid(String uuid) throws BusinessException;

    UserBasic getUserBasicByEmail(String email) throws BusinessException;

    boolean setUserAuth(String email) throws BusinessException;

    ResultInfoEntity updateUserBasic(UserBasic userBasic) throws BusinessException;

    ResultInfoEntity setUserAuthByUrl(String data) throws BusinessException;
}
