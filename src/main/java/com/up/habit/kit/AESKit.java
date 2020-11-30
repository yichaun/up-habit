package com.up.habit.kit;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/11/4 22:21
 */
public class AESKit {
    public static boolean initialized = false;

    public static final String ALGORITHM = "AES/ECB/PKCS7Padding";


    /**
     * TODO:
     *
     * @param str 要被加密的字符串
     * @param key 加/解密要用的长度为32的字节数组（256位）密钥
     * @return byte[]   加密后的字节数组
     * @Author 王剑洪 on 2020/11/4 22:42
     **/
    public static byte[] Aes256Encode(String str, byte[] key) {
        initialize();
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            /**生成加密解密需要的Key*/
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            result = cipher.doFinal(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * TODO:
     *
     * @param bytes 要被解密的字节数组
     * @param key   加/解密要用的长度为32的字节数组（256位）密钥
     * @return java.lang.String 解密后的字符串
     * @Author 王剑洪 on 2020/11/4 22:42
     **/
    public static String Aes256Decode(byte[] bytes, byte[] key) {
        initialize();
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            /**生成加密解密需要的Key*/
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = cipher.doFinal(bytes);
            result = new String(decoded, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
}
