package com.qixi.common.util;

import com.qixi.common.UserBase;
import com.qixi.db.entity.UserBasic;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-2-11
 * Time: 下午11:56
 * To change this template use File | Settings | File Templates.
 */
public class UserUtil {

    /**
     * 将用户的一般信息转成存储在session中的用户信息
     * @param userBasic
     * @return
     */
    public static UserBase convertUserBase(UserBasic userBasic){
        UserBase userBase = new UserBase();
        userBase.setEmail(userBasic.getRegEmail());
        userBase.setId(userBasic.getUid());
        userBase.setUuid(userBasic.getUuid());
        userBase.setNickName(userBasic.getNickName());
        userBase.setIconUrl(userBasic.getAvatar());
        userBase.setState(userBasic.getState());
        userBase.setAuthFlag(getAuthFlag(userBasic.getState()));
        userBase.setFirstLoginFlag(getFirstLoginFlag(userBasic.getState()));

        return userBase;
    }

    /**
     * 通过位运算修改状态，为激活状态
     * 8位中右一代表激活状态，0未激活，1已激活
     * @param state
     * @return
     */
    public static byte setAuthByte(Byte state){
        return (byte) (state | 1);
    }

    /**
     * 通过位运算修改状态，为激活状态
     * 8位中右一代表激活状态，0未激活，1已激活
     * @param state
     * @return
     */
    public static byte setUnAuthByte(Byte state){

        return (byte) (state & 254);
    }

    /**
     * 通过位运算来判断，该用户是否为激活用户
     * @param state
     * @return
     */
    public static boolean getAuthFlag(Byte state){
        byte computedState = (byte) (state & 1);
        return computedState == 1;
    }

    /**
     * 通过位运算修改状态，为初次登陆状态
     * 8位中右二代表初次状态，0是初次，1非初次
     * @param state
     * @return
     */
    public static byte setFirstLoginByte(Byte state){
        return (byte) (state | 2);
    }

    /**
     * 通过位运算修改状态，为初次登陆状态
     * 8位中右二代表激活状态，1是初次，0非初次
     * @param state
     * @return
     */
    public static byte setUnFirstLoginByte(Byte state){
        return (byte) (state & 253);
    }

    /**
     * 通过位运算来获取是否初次访问
     * @param state
     * @return
     */
    public static boolean getFirstLoginFlag(Byte state){
        byte computedState = (byte) (state & 2);
        return computedState == 2;
    }
}
