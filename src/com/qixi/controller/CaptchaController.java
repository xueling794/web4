package com.qixi.controller;


import com.qixi.common.BaseController;
import com.qixi.common.captcha.Captcha;
import com.qixi.common.annotation.RequestAuthentication;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-3
 * Time: 下午10:31
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class CaptchaController extends BaseController {
    private Logger logger = Logger.getLogger(CaptchaController.class);


    @RequestMapping("/captcha/getCaptcha")
    public void getCaptcha(HttpServletRequest req, HttpServletResponse res) {
        try {
            Map<String, Object> resMap = new HashMap<String, Object>();
            nl.captcha.Captcha captcha = new nl.captcha.Captcha.Builder(100, 30)
                    .addBackground(new nl.captcha.backgrounds.FlatColorBackgroundProducer(Color.WHITE))
                    .addText(new nl.captcha.text.renderer.DefaultWordRenderer(Captcha.listColor, Captcha.listFont))
                    .gimp(new nl.captcha.gimpy.StretchGimpyRenderer(0.1, 0.1))
                    .gimp(new DropShadowGimpyRenderer())
                    .build();

            String answer = captcha.getAnswer();
            req.getSession().setAttribute("captcha", answer);
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            BufferedImage challenge = captcha.getImage();
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);

            byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            String cap = Base64.encodeBase64String(captchaChallengeAsJpeg);
            resMap.put("captcha",cap);

            this.successResponse(res, resMap);

        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, e.getMessage());
        }

    }

    @RequestMapping("/captcha/checkCaptcha")
    public void checkCaptcha(HttpServletRequest req, HttpServletResponse res) {
        String data = this.getData(req);
        Map<String,Object> map = this.getModel(data,Map.class);
        String captcha = this.getString(map,"captcha");
        String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
        if(sessionCaptcha != null && sessionCaptcha.equalsIgnoreCase(captcha)){
            map.put("checkResult",true);

        }else{
            map.put("checkResult",false);
        }
        this.successResponse(res,map);
    }
}
