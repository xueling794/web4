package com.qixi.controller;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IEmailService;
import com.qixi.business.service.IUserService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.util.Encrypt;
import com.qixi.db.entity.UserBasic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Time: 下午10:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class EmailController  extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    IEmailService emailService;
    @Autowired
    IUserService userService;

    @RequestMapping("/email/sendEmail")
    public void sendEmail(HttpServletRequest req, HttpServletResponse res) {

        Map<String, Object> resMap = new HashMap<String, Object>();
        boolean sendFlag = false;
        try {
            sendFlag = emailService.sendEmail("dalianyg@126.com","test","test");
            resMap.put("sendResult",sendFlag);
            logger.info("发送邮件成功");
            this.successResponse(res,resMap);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"Send Email Failure");
        }

    }


    @RequestMapping("/email/sendActiveEmail")
    public void sendActiveEmail(HttpServletRequest req, HttpServletResponse res) {

        String data = this.getData(req);
        Map<String,Object> map = this.getModel(data,Map.class);
        String email = this.getString(map,"email");

        boolean sendFlag = false;
        try {
            UserBasic userBasic = userService.getUserBasicByEmail(email);
            if(userBasic == null){
                map.put("sendResult",sendFlag);
                map.put("sendResultMsg", ResultInfo.LOGIN_USER_NO_EXIST);
                logger.warn("Send Active Email :" + email + " ,email is not register");
                this.successResponse(res,map);
            }else{
                String activeData = Encrypt.encodeByDes(email);
                activeData = Encrypt.encryptBase64(activeData);
                ResultInfoEntity resultInfoEntity = emailService.sendActiveEmail(email ,activeData);
                map.put("sendResult",resultInfoEntity.isResultFlag());
                map.put("sendResultMsg",resultInfoEntity.getResultInfo());
                logger.info("Send Active Email :"+email+"  success");
                this.successResponse(res,map);
            }

        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"Send Email Failure");
        }

    }

    @RequestMapping("/email/sendPasswordEmail")
    public void sendPasswordEmail(HttpServletRequest req, HttpServletResponse res) {

        String data = this.getData(req);
        Map<String,Object> map = this.getModel(data,Map.class);
        String email = this.getString(map,"email");

        boolean sendFlag = false;
        try {
            UserBasic userBasic = userService.getUserBasicByEmail(email);
            if(userBasic == null){
                map.put("sendResult",sendFlag);
                map.put("sendResultMsg", ResultInfo.LOGIN_USER_NO_EXIST);
                logger.warn("Send Active Email :" + email + " ,email is not register");
                this.successResponse(res,map);
            }else{
                String passwordData = Encrypt.encodeByDes(email +Encrypt.splitStr+ userBasic.getPassword());
                passwordData = Encrypt.encryptBase64(passwordData);
                ResultInfoEntity resultInfoEntity = emailService.sendPasswordEmail(email ,userBasic.getNickName(),passwordData);
                map.put("sendResult",resultInfoEntity.isResultFlag());
                map.put("sendResultMsg",resultInfoEntity.getResultInfo());
                logger.warn("Send Active Email :"+email+"  success");
                this.successResponse(res,map);
            }

        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"Send Email Failure");
        }

    }
}
