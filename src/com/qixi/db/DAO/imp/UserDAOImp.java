package com.qixi.db.DAO.imp;


import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.BaseDAO;
import com.qixi.common.Exception.DBException;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.constant.UserConst;
import com.qixi.common.util.Encrypt;
import com.qixi.common.util.UserUtil;
import com.qixi.db.DAO.Service.IUserDAO;
import com.qixi.db.entity.UserBasic;
import com.qixi.db.entity.UserBasicExample;
import com.qixi.db.mapper.UserBasicMapper;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-7
 * Time: 下午11:40
 * To change this template use File | Settings | File Templates.
 */
public class UserDAOImp extends BaseDAO implements IUserDAO {

    @Override
    public ResultInfoEntity validateUser(String user, String password) throws DBException {
        try {
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.LOGIN_USER_NULL);
                return resultInfoEntity;
            }
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andRegEmailEqualTo(user);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList == null || userBasicList.size() != 1) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.LOGIN_USER_NO_EXIST);
                return resultInfoEntity;
            }
            UserBasic userBasic = userBasicList.get(0);
            String randomKey = userBasic.getRandomKey();
            String userPassword = userBasic.getPassword();
            String mixPassword = Encrypt.mixPasswordWithKey(password, randomKey);
            if (userPassword != null && userPassword.equals(mixPassword)) {
                //记录用户最后登录的时间
                userBasic.setLastLoginTime(new Date());
                userBasicMapper.updateByPrimaryKey(userBasic);
                resultInfoEntity.setResultFlag(true);
                return resultInfoEntity;
            } else {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.LOGIN_USER_PASSWORD_ERROR);
                return resultInfoEntity;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public int addNewUser(String email, String password, String randomKey) throws DBException {
        try {
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password) || StringUtils.isEmpty(randomKey)) {
                return 0;
            }
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            String mixPassword = Encrypt.mixPasswordWithKey(password, randomKey);
            String uuid = UUID.randomUUID().toString();
            UserBasic userBasic = new UserBasic();
            userBasic.setUuid(uuid);
            userBasic.setRegEmail(email);
            userBasic.setPassword(mixPassword);
            userBasic.setRandomKey(randomKey);
            //userBasic.setState(UserConst.USER_STATE_AUTH);
            userBasic.setSignUpTime(new Date());
            userBasic.setGender(UserConst.USER_GENDER_FEMALE);
            int insertNum = userBasicMapper.insert(userBasic);
            return insertNum > 0 ? insertNum : 0;
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public ResultInfoEntity addNoAuthUser(String email, String password, String randomKey) throws DBException {
        try {
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password) || StringUtils.isEmpty(randomKey)) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.REG_USER_NULL);
                return resultInfoEntity;
            }
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andRegEmailEqualTo(email);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList != null && userBasicList.size() > 0) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.REG_USER_EXIST);
                return resultInfoEntity;
            }
            String mixPassword = Encrypt.mixPasswordWithKey(password, randomKey);
            String uuid = UUID.randomUUID().toString();
            UserBasic userBasic = new UserBasic();
            userBasic.setUuid(uuid);
            userBasic.setRegEmail(email);
            userBasic.setNickName(email);
            userBasic.setPassword(mixPassword);
            userBasic.setRandomKey(randomKey);
            userBasic.setSignUpTime(new Date());

            //用户刚注册的状态
            userBasic.setState(UserConst.NEW_USER_STATE);
            //用户默认性别女性
            userBasic.setGender(UserConst.USER_GENDER_FEMALE);
            //设置用户的默认头像
            userBasic.setAvatar(UserConst.USER_DEFAULT_AVATAR);

            int insertNum = userBasicMapper.insert(userBasic);
            if (insertNum > 0) {
                resultInfoEntity.setResultFlag(true);
                resultInfoEntity.setResultInfo(insertNum + "");
                return resultInfoEntity;
            } else {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.REG_SERVICE_ERROR);
                return resultInfoEntity;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public ResultInfoEntity changePassword(String email, String originPassword, String newPassword) throws DBException {
        try {
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(originPassword) || StringUtils.isEmpty(newPassword)) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo("");
                return resultInfoEntity;
            }
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andRegEmailEqualTo(email);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList == null || userBasicList.size() != 1) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.REG_NO_USER);
                return resultInfoEntity;
            }
            UserBasic userBasic = userBasicList.get(0);
            String randomKey = userBasic.getRandomKey();
            String userPassword = userBasic.getPassword();
            String mixPassword = Encrypt.mixPasswordWithKey(originPassword, randomKey);
            if (userPassword != null && userPassword.equals(mixPassword)) {
                String newMixPassword = Encrypt.mixPasswordWithKey(newPassword, randomKey);
                userBasic.setPassword(newMixPassword);
                int num = userBasicMapper.updateByPrimaryKey(userBasic);
                if (num > 0) {
                    resultInfoEntity.setResultFlag(true);
                    resultInfoEntity.setResultInfo(ResultInfo.USER_CHANGE_PSWD_SUCCESS);
                    return resultInfoEntity;
                } else {
                    resultInfoEntity.setResultFlag(false);
                    resultInfoEntity.setResultInfo(ResultInfo.USER_CHANGE_PSWD_ERROR);
                    return resultInfoEntity;
                }
            } else {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.USER_CHANGE_PSWD_CHECK);
                return resultInfoEntity;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public UserBasic getUserBasicByUid(int uid) throws DBException {
        try {
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andUidEqualTo(uid);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList != null && userBasicList.size() > 0) {
                return userBasicList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public UserBasic getUserBasicByUuid(String uuid) throws DBException {
        try {
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andUuidEqualTo(uuid);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList != null && userBasicList.size() > 0) {
                return userBasicList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public UserBasic getUserBasicByEmail(String email) throws DBException {
        try {
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andRegEmailEqualTo(email);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList != null && userBasicList.size() > 0) {
                return userBasicList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public boolean setUserAuth(String email) throws DBException {
        try {
            UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
            UserBasicExample userBasicExample = new UserBasicExample();
            UserBasicExample.Criteria criteria = userBasicExample.createCriteria();
            criteria.andRegEmailEqualTo(email);
            List<UserBasic> userBasicList = userBasicMapper.selectByExample(userBasicExample);
            if (userBasicList != null && userBasicList.size() > 0) {
                UserBasic userBasic = userBasicList.get(0);
                Byte userState = userBasic.getState();
                userBasic.setState(UserUtil.setAuthByte(userState));
                int num = userBasicMapper.updateByPrimaryKey(userBasic);
                return num > 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public ResultInfoEntity updateUserBasic(UserBasic userBasic) throws DBException {
        try {
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            if (userBasic == null) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.LOGIN_USER_NULL);
                return resultInfoEntity;
            } else {
                UserBasic userBasicTemp = this.getUserBasicByUuid(userBasic.getUuid());
                if (userBasicTemp == null) {
                    resultInfoEntity.setResultFlag(false);
                    resultInfoEntity.setResultInfo(ResultInfo.LOGIN_USER_NULL);
                    return resultInfoEntity;
                } else {
                    UserBasicMapper userBasicMapper = (UserBasicMapper) this.getMapperClass();
                    int num = userBasicMapper.updateByPrimaryKey(userBasic);
                    if (num > 0) {
                        resultInfoEntity.setResultFlag(true);
                        return resultInfoEntity;
                    } else {
                        resultInfoEntity.setResultFlag(false);
                        resultInfoEntity.setResultInfo(ResultInfo.USERBASIC_UPDATE_FAILURE);
                        return resultInfoEntity;
                    }
                }
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }


    @Override
    public ResultInfoEntity setUserAuthByUrl(String data) throws DBException {
        try {
            String dataBase64 = Encrypt.decryptBase64(data);
            String email = Encrypt.decodeByDes(dataBase64);
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            UserBasic userBasic = this.getUserBasicByEmail(email);
            if (userBasic == null) {
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.REG_NO_USER);
                return resultInfoEntity;
            } else {
                boolean authFlag = UserUtil.getAuthFlag(userBasic.getState());
                if (authFlag) {
                    resultInfoEntity.setResultFlag(false);
                    resultInfoEntity.setResultInfo(ResultInfo.USER_DO_ACTIVE_AGAIN);
                    return resultInfoEntity;
                } else {
                    Byte authByte = UserUtil.setAuthByte(userBasic.getState());
                    userBasic.setState(authByte);
                    ResultInfoEntity resultInfoEntityTemp = this.updateUserBasic(userBasic);
                    if (resultInfoEntityTemp.isResultFlag()) {
                        resultInfoEntityTemp.setResultInfo(email);
                    }
                    return resultInfoEntityTemp;

                }
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }


}
