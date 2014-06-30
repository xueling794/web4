package com.qixi.business.imp;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IUserService;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.util.Encrypt;
import com.qixi.db.DAO.Service.IUserDAO;
import com.qixi.db.entity.UserBasic;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-7
 * Time: 下午11:35
 * To change this template use File | Settings | File Templates.
 */
public class UserServiceImp  implements IUserService{

    private IUserDAO userDAO;

    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public ResultInfoEntity validateUser(String user, String password) throws BusinessException {
        return userDAO.validateUser(user,password);
    }

    @Override
    public int addNewUser(String email, String password, String randomKey) throws BusinessException {
        return userDAO.addNewUser(email,password,randomKey);
    }

    @Override
    public ResultInfoEntity changePassword(String email, String originPassword, String newPassword) throws BusinessException {
        return userDAO.changePassword(email,originPassword,newPassword);
    }

    @Override
    public ResultInfoEntity addNoAuthUser(String email, String password, String randomKey) throws BusinessException {
        return userDAO.addNoAuthUser(email,password,randomKey);
    }

    @Override
    public UserBasic getUserBasicByUid(int uid) throws BusinessException {
        return userDAO.getUserBasicByUid(uid) ;
    }

    @Override
    public UserBasic getUserBasicByUuid(String uuid) throws BusinessException {
        return userDAO.getUserBasicByUuid(uuid);
    }

    @Override
    public UserBasic getUserBasicByEmail(String email) throws BusinessException {
        return userDAO.getUserBasicByEmail(email);
    }

    @Override
     public boolean setUserAuth(String email) throws BusinessException {
        return userDAO.setUserAuth(email);
    }

    @Override
    public ResultInfoEntity setUserAuthByUrl(String data) throws BusinessException {
        return userDAO.setUserAuthByUrl(data);
    }

    @Override
    public ResultInfoEntity updateUserBase(UserBasic userBasic)throws BusinessException{
        return userDAO.updateUserBasic(userBasic) ;
    }

    @Override
    public  ResultInfoEntity resetUserPassword(String data ,String newPassword ,String randomKey) throws  BusinessException{
        String decodeBase64 = Encrypt.decryptBase64(data);
        String explainText = Encrypt.decodeByDes(decodeBase64);
        String[] paramArray = explainText.split(Encrypt.splitStr);
        ResultInfoEntity resultInfoEntity =  new ResultInfoEntity();
        if(paramArray != null && paramArray.length ==2){
            String email = paramArray[0];
            UserBasic userBasic = userDAO.getUserBasicByEmail(email);
            if(userBasic == null){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.USER_RESET_PSWD_ERROR);
                return resultInfoEntity;
            } else{
                String mixPassword = Encrypt.mixPasswordWithKey(newPassword,randomKey);
                userBasic.setPassword(mixPassword);
                userBasic.setRandomKey(randomKey);
                ResultInfoEntity resTemp = userDAO.updateUserBasic(userBasic);
                if(resTemp.isResultFlag()){
                    resultInfoEntity.setResultFlag(true);
                    resultInfoEntity.setResultInfo(ResultInfo.USER_RESET_PSWD_SUCCESS);
                    return resultInfoEntity;
                } else{
                    resultInfoEntity.setResultFlag(false);
                    resultInfoEntity.setResultInfo(ResultInfo.USER_RESET_PSWD_ERROR);
                    return resultInfoEntity;
                }
            }
        }else{
            resultInfoEntity.setResultFlag(false);
            resultInfoEntity.setResultInfo(ResultInfo.USER_RESET_PSWD_ERROR);
            return resultInfoEntity;
        }
    }
}
