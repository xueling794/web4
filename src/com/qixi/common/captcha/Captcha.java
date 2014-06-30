package com.qixi.common.captcha;

import java.awt.*;
import java.util.ArrayList;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-3
 * Time: 下午10:36
 * To change this template use File | Settings | File Templates.
 */
public class Captcha {

    public static final int CAPTCHA_SIMPLE_TEXT_DEFAULT_LEN						= 4;
    public static final char[] CAPTCHA_SIMPLE_TEXT_ARRAY_CHAR					=
            {
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'L', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    '1', '2', '3', '4', '5', '6', '7', '8', '9'
            };
    public static final ArrayList<Color> listColor = new ArrayList<Color>();
    public static final ArrayList<Font> listFont = new ArrayList<Font>();

    static {
        listColor.add(Color.BLACK);
        listFont.add(new Font(Font.SERIF, Font.ITALIC, 30));

    }

}
