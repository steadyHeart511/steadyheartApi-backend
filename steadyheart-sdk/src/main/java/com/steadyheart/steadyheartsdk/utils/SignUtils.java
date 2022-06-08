package com.steadyheart.steadyheartsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author lts
 * @create 2023-10-11 14:13
 */
public class SignUtils {

    public static String sign(String secret,String body) {
        Digester digester = new Digester(DigestAlgorithm.MD5);
        //  加密后数据
        String encryption = digester.digestHex(body + "." + secret);
        return encryption;
    }
}
