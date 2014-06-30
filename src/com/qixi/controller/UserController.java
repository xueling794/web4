package com.qixi.controller;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IEmailService;
import com.qixi.business.service.IUserService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.UserBase;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.constant.UserConst;
import com.qixi.common.util.Encrypt;
import com.qixi.common.util.UserUtil;
import com.qixi.db.entity.UserBasic;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-4
 * Time: 下午10:22
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class UserController extends BaseController {
    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    IUserService userService;

    @Autowired
    IEmailService emailService;

    @RequestMapping("/user/validateUser")
    public void validateUser(HttpServletRequest req, HttpServletResponse res) {
        String data = this.getData(req);
        Map<String,Object> map = this.getModel(data,Map.class);
        String user = this.getString(map,"user");
        String password = this.getString(map,"password");
        if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password)){
            map.put("validateResult",false);
            map.put("validateResultMsg" ,ResultInfo.LOGIN_USER_NULL);
            this.successResponse(res,map);
            return;
        }
        ResultInfoEntity resultInfoEntity ;
        try {
            resultInfoEntity = userService.validateUser(user,password);
            if(resultInfoEntity.isResultFlag()){

                UserBasic userBasic = userService.getUserBasicByEmail(user);
                UserBase userBase = UserUtil.convertUserBase(userBasic);
                if(userBase.isAuthFlag()){
                    req.getSession().setAttribute(UserConst.USER_BASE,userBase);
                }
                map.put("userBase",userBase);
            }

            map.put("validateResult",resultInfoEntity.isResultFlag());
            map.put("validateResultMsg" , resultInfoEntity.getResultInfo());
            this.successResponse(res,map);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,ResultInfo.LOGIN_SERVICE_ERROR);
        }
    }

    @RequestMapping("/user/getUserSession")
    public void getUserSession(HttpServletRequest req, HttpServletResponse res) {
        UserBase userBase = getUserBase(req);
        Map<String,Object> map = new HashMap<String, Object>();
        if(userBase == null){
            map.put("isLogin",false);
        }else{
            map.put("isLogin",true);
            map.put("userBase" , userBase);
        }
        this.successResponse(res,map);
    }

    @RequestMapping("/user/doLogOut")
    public void doLogOut(HttpServletRequest req, HttpServletResponse res) {
        UserBase userBase = (UserBase) req.getSession().getAttribute(UserConst.USER_BASE);
        req.getSession().setAttribute(UserConst.USER_BASE, null);
        req.getSession().invalidate();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("isLogin",false);
        logger.info(userBase.getEmail()+" do log out");
        this.successResponse(res,map);
    }

    @RequestMapping("/user/addNoAuthUser")
    public void addNoAuthUser(HttpServletRequest req, HttpServletResponse res) {
        String data = this.getData(req);
        Map<String,Object> map = this.getModel(data,Map.class);
        String user = this.getString(map,"user");
        String password = this.getString(map,"password");
        String randomKey = this.getString(map,"randomKey");

        if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password) || StringUtils.isEmpty(randomKey)){
            map.put("result",false);
            map.put("resultMsg" , ResultInfo.REG_USER_NULL);
            this.successResponse(res,map);
            return;
        }
        //验证验证码
        String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
        if(sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(randomKey)){
            map.put("result",false);
            map.put("resultMsg",ResultInfo.REG_CAPTCHA_ERROR);
            this.successResponse(res,map);
            return;
        }
        try {
            ResultInfoEntity resultInfoEntity = userService.addNoAuthUser(user,password,randomKey);

            if(resultInfoEntity.isResultFlag()){
                String activeData = Encrypt.encodeByDes(user);
                activeData = Encrypt.encryptBase64(activeData);
                emailService.sendActiveEmail(user , activeData);
                map.put("result",true);
                map.put("uid" , resultInfoEntity.getResultInfo());
                this.successResponse(res,map);
                return;
            }else{
                map.put("result",false);
                map.put("resultMsg",resultInfoEntity.getResultInfo());
                this.successResponse(res,map);
                return;
            }
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,ResultInfo.REG_SERVICE_ERROR);
        }
    }

    @RequestMapping("/user/getSelfUserInfo")
    public void getSelfUserInfo(HttpServletRequest req, HttpServletResponse res) {
         UserBase userBase = this.getUserBase(req);
         Map<String,Object> map = new HashMap<String, Object>();
        try {
            if(userBase == null){
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }
            String uuid = userBase.getUuid();
            UserBasic userBasic = userService.getUserBasicByUuid(uuid);
            map.put("userBasic",userBasic);
            this.successResponse(res,map);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"查询用户信息失败");
        }

    }

    @RequestMapping("/user/changePassword")
    public void changePassword(HttpServletRequest req, HttpServletResponse res) {
        try {
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            String user = this.getUserBase(req).getEmail();
            String password = this.getString(map,"password");
            String newPassword = this.getString(map,"newPassword");
            if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword)){
                map.put("result",false);
                this.successResponse(res,map);
                return;
            }

            ResultInfoEntity resultInfoEntity = userService.changePassword(user,password,newPassword);
            map.put("result",resultInfoEntity.isResultFlag());
            map.put("resultMsg",resultInfoEntity.getResultInfo());
            this.successResponse(res,map);
            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"修改密码失败");
        }
    }

    @RequestMapping("/user/activeUser")
    public void activeUser(HttpServletRequest req, HttpServletResponse res) {
        String data = this.getData(req);

        Map<String,Object> resMap = new HashMap<String, Object>();
        try {
            Map<String,Object> map = this.getModel(data,Map.class);
            String param = this.getString(map,"data");
            ResultInfoEntity resultInfoEntity = userService.setUserAuthByUrl(param);
            if(resultInfoEntity.isResultFlag()){
                //激活成功后直接实现登陆
                String email = resultInfoEntity.getResultInfo();
                UserBasic userBasic = userService.getUserBasicByEmail(email);
                UserBase userBase = UserUtil.convertUserBase(userBasic);
                req.getSession().setAttribute(UserConst.USER_BASE,userBase);
            }
            resMap.put("activeResult",resultInfoEntity.isResultFlag());
            resMap.put("activeResultMsg",resultInfoEntity.getResultInfo());
            this.successResponse(res,resMap);
            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"激活用户失败");
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"激活用户失败");
        }




    }


    @RequestMapping("/user/resetUserPassword")
    public void resetUserPassword(HttpServletRequest req, HttpServletResponse res) {
        String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
        String data = this.getData(req);

        try {
            Map<String,Object> map = this.getModel(data,Map.class);
            String randomKey = this.getString(map,"randomKey");

            if(randomKey == null || !randomKey.equals(sessionCaptcha)){
                map.put("result", false);
                map.put("resultMsg",ResultInfo.REG_CAPTCHA_ERROR);
                this.successResponse(res,map);
                return;
            }
            String encodeData = this.getString(map,"encodeData");
            String newPassword = this.getString(map,"newPassword");
            ResultInfoEntity resultInfoEntity = userService.resetUserPassword(encodeData, newPassword, randomKey);

            map.put("resetPasswordResult",resultInfoEntity.isResultFlag());
            map.put("resetPasswordMsg",resultInfoEntity.getResultInfo());
            this.successResponse(res,map);
            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"激活用户失败");
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"激活用户失败");
        }




    }

}
