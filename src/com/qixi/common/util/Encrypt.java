package com.qixi.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-4
 * Time: 下午11:30
 * To change this template use File | Settings | File Templates.
 */
public class Encrypt {
    private static String desKey = "xueling0";
    private static final Logger logger = Logger.getLogger(Encrypt.class);
    public static  String splitStr = ";";

    /**
     * 将密码进行加密处理
     * @param password
     * @return
     */
    public static String mixPassword(String password){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update((password + desKey).getBytes());
            byte messageDigest[] = md5.digest();
            return Base64.encodeBase64String(messageDigest);
        } catch (Exception ex) {
            logger.error("Error" ,ex);
            return null;
        }
    }

    public static String mixPasswordWithKey(String password ,String key){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update((password + key).getBytes());
            byte messageDigest[] = md5.digest();
            return Base64.encodeBase64String(messageDigest);
        } catch (Exception ex) {
            logger.error("Error" ,ex);
            return null;
        }
    }

    /**
     * 将字符串转换成Base64格式
     * @param value
     * @return
     */
    public static String encryptBase64(String value) {
        return Base64.encodeBase64String(value.getBytes());
    }

    /**
     * 将Base64的字符串转换成普通字符串
     * @param EncryptedString
     * @return
     */
    public static String decryptBase64(String EncryptedString) {
        return new String(Base64.decodeBase64(EncryptedString));
    }

    /**
     * 转换字符串为16进制
     *
     * @param ss
     * @return
     */
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;

    }

    /**
     * 16进制的byte数组转换成字符串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    /**
     * 通过多DES算法对铭文进行加密
     *
     * @param plainText
     * @return
     */
    public static String encodeByDes(String plainText) {
        try {
            if (!plainText.isEmpty()) {
                Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

                SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), "DES");
                cipher.init(Cipher.ENCRYPT_MODE, key);

                String cipherText = toHexString(cipher.doFinal(plainText.getBytes("UTF-8")));

                return cipherText;
            } else {
                return "";
            }
        } catch (Exception ex) {
            logger.error("Error", ex);
            return "";
        }
    }

    /**
     * 对DES加密后密文进行解密
     *
     * @param cipherText
     * @return
     */
    public static String decodeByDes(String cipherText) {
        String plainText;
        try {
            if (!cipherText.isEmpty()) {
                byte[] byteSrc = convertHexString(cipherText);
                SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), "DES");
                Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);

                byte[] retByte = cipher.doFinal(byteSrc);
                plainText = new String(retByte);
                return plainText;
            } else {
                return "";
            }
        } catch (Exception ex) {
            logger.error("Error", ex);
            return "";
        }

    }

}
