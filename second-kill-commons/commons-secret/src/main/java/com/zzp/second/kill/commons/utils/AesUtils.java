package com.zzp.second.kill.commons.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author 佐斯特勒
 * <p>
 *  AES对称加密工具类
 * </p>
 * @version v1.0.0
 * @date 2020/1/17 14:48
 * @see  AesUtils
 **/
public class AesUtils {
    private static String Algorithm = "AES";
    /**
     * AES 算法
     * ECB 模式
     * PKCS5Padding 补码方式
     */
    private static String AlgorithmProvider = "AES/ECB/PKCS5Padding";

    /**
     * Ase加密
     * @param str 随机码字符串
     * @param key 加密键
     * @return 加密后的数组
     * @throws NoSuchPaddingException 。
     * @throws NoSuchAlgorithmException。
     * @throws InvalidKeyException。
     * @throws BadPaddingException。
     * @throws IllegalBlockSizeException。
     */
    public static byte[] encrypt(String str,byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        var secretKeySpec = new SecretKeySpec(key, Algorithm);
        var cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Ase解密
     * @param str 随机码字符串
     * @param key 加密键
     * @return 解密后的数组
     * @throws NoSuchPaddingException 。
     * @throws NoSuchAlgorithmException。
     * @throws InvalidKeyException。
     * @throws BadPaddingException。
     * @throws IllegalBlockSizeException。
     */
    public static byte[] decrypt(String str,byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        var secretKeySpec = new SecretKeySpec(key, Algorithm);
        var cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        var hexBytes = hexStringToBytes(str);
        return cipher.doFinal(hexBytes);
    }

    /**
     * 把字符串转成16进制
     * @param str String
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String str){
        // 字符串字母大写
        var hex = str.toUpperCase();
        var length = hex.length() / 2;
        var charArray = hex.toCharArray();
        var bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            bytes[i] = (byte)(charToByte(charArray[pos])<<4 | charToByte(charArray[pos + 1]));
        }
        return bytes;
    }

    /**
     * char转byte
     * @param c char字符
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * byte转字符串
     * @param src byte参数
     * @return String
     */
    public static String byteToString(byte[] src){
        var sb = new StringBuilder();
        for (byte value : src) {
            var b = value & 0xff;
            var hexString = Integer.toHexString(b);
            if (hexString.length() < 2) {
                sb.append("0");
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    /**
     * 测试工具类
     */
    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        String param = "帅气的周志鹏";
        var s = byteToString(encrypt(param, "asdfghjklzxcvbnm".getBytes()));
        System.out.println("这是加密后的：\n"+s);
        System.out.println("=======================================================");
        System.out.println("解密后：\n"+new String(decrypt(s, "asdfghjklzxcvbnm".getBytes()), StandardCharsets.UTF_8));
    }
}
