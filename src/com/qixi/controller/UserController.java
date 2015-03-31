package com.qixi.controller;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IEmailService;
import com.qixi.business.service.IImageService;
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
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-4
 * Time: 下午10:22
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class UserController extends BaseController {
    private static final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    IUserService userService;

    @Autowired
    IEmailService emailService;

    @Autowired
    IImageService imageService;

    @RequestMapping("/user/validateUser")
    public void validateUser(HttpServletRequest req, HttpServletResponse res) {
        String data = this.getData(req);
        Map<String,Object> map = this.getModel(data,Map.class);
        String user = this.getString(map,"user");
        String password = this.getString(map,"password");
        if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password)){
            map.put("validateResult",false);
            map.put("validateResultMsg" ,ResultInfo.LOGIN_USER_NULL);
            logger.warn(ResultInfo.LOGIN_USER_NULL);
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
            logger.info("用户登录成功:"+user);
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
            logger.info("用户未登录");
            map.put("isLogin",false);
        }else{
            logger.info("用户已登录");
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
        logger.info("用户成功注销"+userBase.getEmail());
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
            logger.warn(ResultInfo.REG_USER_NULL);
            this.successResponse(res,map);
            return;
        }
        //验证验证码
        String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
        if(sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(randomKey)){
            map.put("result",false);
            map.put("resultMsg",ResultInfo.REG_CAPTCHA_ERROR);
            logger.warn(ResultInfo.REG_CAPTCHA_ERROR);
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
                logger.info("用户注册成功:"+user);
                this.successResponse(res,map);
                return;
            }else{
                map.put("result",false);
                map.put("resultMsg",resultInfoEntity.getResultInfo());
                logger.info("用户注册失败:"+user);
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
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }
            String uuid = userBase.getUuid();
            UserBasic userBasic = userService.getUserBasicByUuid(uuid);
            UserBasic newUserBasic = UserUtil.getOpenUserBasic(userBasic);

            logger.info("查询用户本人信息成功:"+userBasic.getRegEmail());
            map.put("userBasic",newUserBasic);
            this.successResponse(res,map);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"查询用户本人信息失败");
        }

    }

    @RequestMapping("/user/changePassword")
    public void changePassword(HttpServletRequest req, HttpServletResponse res) {
        try {
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            String user = this.getUserBase(req).getEmail();
            String password = this.getString(map,"password");
            String newPassword = this.getString(map,"newPassword");
            if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword)){
                map.put("result",false);
                logger.warn(ResultInfo.USER_CHANGE_PSWD_ERROR);
                this.successResponse(res,map);
                return;
            }

            ResultInfoEntity resultInfoEntity = userService.changePassword(user,password,newPassword);
            map.put("result",resultInfoEntity.isResultFlag());
            map.put("resultMsg",resultInfoEntity.getResultInfo());
            logger.info("修改密码成功:"+user);
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
                logger.info(resultInfoEntity.getResultInfo()+email);
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

    @RequestMapping("/user/updateUser")
    public void udpateUser(HttpServletRequest req, HttpServletResponse res){
        try {
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            String user = this.getUserBase(req).getEmail();

            String nickName = this.getString(map,"nickName");
            String birthday = this.getString(map,"birthday");
            int genderInt = this.getInteger(map,"gender");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(birthday);
            String signature = this.getString(map,"signature");
            if(StringUtils.isEmpty(user) || StringUtils.isEmpty(nickName) ){
                map.put("result",false);
                logger.warn("用户昵称格式错误:"+user);
                this.successResponse(res,map);
                return;
            }
            UserBasic userBasic = userService.getUserBasicByEmail(user);

            userBasic.setNickName(nickName);
            userBasic.setBirthday(date);
            userBasic.setGender((byte)genderInt);
            userBasic.setSignature(signature);

            ResultInfoEntity resultInfoEntity = userService.updateUserBase(userBasic);
            map.put("result",resultInfoEntity.isResultFlag());
            map.put("resultMsg",resultInfoEntity.getResultInfo());
            logger.info(resultInfoEntity.getResultInfo() + user);
            this.successResponse(res,map);
            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"修改个人资料失败");
        } catch(ParseException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"修改个人资料失败");
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
                logger.warn(ResultInfo.REG_CAPTCHA_ERROR);
                this.successResponse(res,map);
                return;
            }
            String encodeData = this.getString(map,"encodeData");
            String newPassword = this.getString(map,"newPassword");
            ResultInfoEntity resultInfoEntity = userService.resetUserPassword(encodeData, newPassword, randomKey);

            map.put("resetPasswordResult",resultInfoEntity.isResultFlag());
            map.put("resetPasswordMsg",resultInfoEntity.getResultInfo());
            logger.info(resultInfoEntity.getResultInfo());
            this.successResponse(res,map);
            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"重置密码失败");
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"重置密码失败");
        }
    }

    @RequestMapping("/user/setAvatar")
    public void setAvatar(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getPostData(req);
            Map<String,Object> map = this.getModel(data, Map.class);
            String imageData = this.getString(map,"imageData");
            int uid = this.getUserBase(req).getId();
            if(imageData == null || !imageData.startsWith("data:image")) {
                logger.error(ResultInfo.USER_AVATAR_IMAGE_ERROR);
                this.failResponse(res,"数据错误，上传头像失败");
                return;
            }

            int subIndex = imageData.indexOf("base64,")+"baase64,".length()-1;
            imageData = imageData.substring(subIndex,imageData.length() -1);
            BASE64Decoder  base64Decoder = new BASE64Decoder();
            byte[] imageBytes = base64Decoder.decodeBuffer(imageData);

            String uuid = UUID.randomUUID().toString();
            String absolutePath = new File(UserController.class.getResource("/").getPath()).getParent();
            File fileTemp = new File(absolutePath+"/upload/avatar/"+uuid);
            fileTemp.createNewFile();
            FileOutputStream fos = new FileOutputStream(absolutePath+"/upload/avatar/"+uuid) ;
            fos.write(imageBytes);
            fos.close();
            FileInputStream fis = new FileInputStream(absolutePath+"/upload/avatar/"+uuid);

            String imageUrl = imageService.saveImage(fis,"1.jpg","test",null);
            if(StringUtils.isEmpty(imageUrl)){
                imageUrl = "/img/defaultAvatar.jpg";
            }else{
                imageUrl = "/image/"+imageUrl+"._png";
            }

            UserBasic userBasic = userService.getUserBasicByUid(uid);
            userBasic.setAvatar(imageUrl);
            ResultInfoEntity resultInfoEntity = userService.updateUserBase(userBasic);
            map.put("setAvatarResult",resultInfoEntity.isResultFlag());
            map.put("setAvatardMsg",resultInfoEntity.getResultInfo());
            logger.info(resultInfoEntity.getResultInfo());
            this.successResponse(res,map);
            return;

        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"数据错误，上传头像失败");
        }
    }

    @RequestMapping("/user/getProfile")
    public void getProfile(HttpServletRequest req, HttpServletResponse res) {
        try{
            String data = this.getPostData(req);

            Map<String,Object> map = this.getModel(data,Map.class);
            int uid = this.getInt(map,"uid");
            UserBasic userBasic = userService.getUserBasicByUid(uid);
            UserBasic newUserBassic = UserUtil.getOpenUserBasic(userBasic);

            logger.info("获取用户信息成功:"+userBasic.getRegEmail());
            map.put("userBasic",newUserBassic);
            this.successResponse(res,map);
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"获取用户信息失败");
        }

    }
}
