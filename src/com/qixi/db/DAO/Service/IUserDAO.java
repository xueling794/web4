package com.qixi.db.DAO.Service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.DBException;
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
     * @throws DBException
     */
    public ResultInfoEntity validateUser(String user, String password) throws DBException;

    /**
     *添加一个已经激活的用户
     * @param email
     * @param password
     * @param randomKey
     * @return
     * @throws DBException
     */
    int addNewUser(String email, String password, String randomKey) throws DBException;

    /**
     * 用户用新密码替换旧密码
     * @param email
     * @param originPassword
     * @param newPassword
     * @return
     * @throws DBException
     */
    ResultInfoEntity changePassword(String email, String originPassword, String newPassword) throws DBException;

    /**
     * 添加一个为激活的用户
     * @param email
     * @param password
     * @param randomKey
     * @return
     * @throws DBException
     */
    ResultInfoEntity addNoAuthUser(String email, String password, String randomKey) throws DBException;

    UserBasic getUserBasicByUid(int uid) throws DBException;

    UserBasic getUserBasicByUuid(String uuid) throws DBException;

    UserBasic getUserBasicByEmail(String email) throws DBException;

    boolean setUserAuth(String email) throws DBException;

    ResultInfoEntity updateUserBasic(UserBasic userBasic) throws DBException;

    ResultInfoEntity setUserAuthByUrl(String data) throws DBException;
}
