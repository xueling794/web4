package com.qixi.common.captcha;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-3
 * Time: 下午11:24
 * To change this template use File | Settings | File Templates.
 */
public class CaptchaUtil {
    private static ImageCaptchaService m_hCaptchaService = new DefaultManageableImageCaptchaService();
    public static final int CAPTCHA_JCAPTCHA			= 0;
    public static final int CAPTCHA_SIMPLECAPTCHA		= 1;
    public static final int CAPTCHA_TYPE				= CAPTCHA_SIMPLECAPTCHA;

    public static final ImageCaptchaService getInstance() {
        return m_hCaptchaService;
    }

}
