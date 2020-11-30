package com.up.habit.kit;

import java.util.Base64;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/11/4 22:20
 */
public class Base64Kit {

    /**
     * 解码
     * @param encodedText
     * @return
     */
    public static byte[] decode(String encodedText){
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }
    /**
     * 编码
     * @param data
     * @return
     */
    public static String encode(byte[] data){
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
