package com.example.csgs.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {
    /**
     * 用java原生的摘要实现SHA256加密
     * @param str 加密前的报文
     */
    public static String getSHA256String(String str) {
        String encodeStr = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * byte[]转为16进制
     *
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte aByte : bytes) {
            String temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}