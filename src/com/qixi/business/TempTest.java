package com.qixi.business;

import com.qixi.common.util.Encrypt;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-23
 * Time: 上午12:02
 * To change this template use File | Settings | File Templates.
 */



public class TempTest {
    /**
     * 转换字符串为16进制
     *
     * @param ss
     * @return
     */
    public  byte[] convertHexString(String ss) {
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
    public  String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    public static void main(String[] args) {
        /*String desKey = "xueling0";
        String origin ="NzhmZGI3MmIyNDZjNzZhY2VjYzlhNDAxYzM1NzIyOTExOWVhYzNhMGVmM2MxMWZlMDQ1NWE0YjZjNDUxMjUwZWJkOGY2MzE5MWY4YjQzYTM1ZTY0N2VkNzYxMmVhZmZm";
        //origin.getBytes();
        SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), "DES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] retByte = cipher.doFinal(origin.getBytes());
            String plainText = new String(retByte);
            *//*byte[] byteSrc = Encrypt.convertHexString(origin);*//*
            System.out.println(plainText);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

        String encodeStr = Encrypt.encodeByDes("dalianyg@126.com|Pl7dmV1f+9MdAUfpz6WdfA==");
        System.out.println(encodeStr);
        String decodeStr = Encrypt.decodeByDes(encodeStr);
        System.out.println(decodeStr);

    }


}
