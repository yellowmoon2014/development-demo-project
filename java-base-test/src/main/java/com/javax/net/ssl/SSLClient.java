package com.javax.net.ssl;

import com.java.net.Client;
import com.java.security.MD5Util;
import com.java.security.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;

public class SSLClient {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Object obj = Client.invokeSync("com.javax.net.ssl.SSLServer",
                "callFirst",
                "java.util.Map",
                Map.of("syn", String.valueOf(Math.random())));
        Map<String, Object> map = (Map<String, Object>) obj;
        String randomKey = UUID.randomUUID().toString();
        String randomKeyEncrypt = RSAUtil.encrypt(randomKey.getBytes(), map.get("publicKey").toString());
        Client.invokeSync("com.javax.net.ssl.SSLServer",
                "callSecond",
                "java.util.Map",
                Map.of("randomKeyEncrypt", randomKeyEncrypt));

        String text = map.get("syn") + map.get("ack").toString() + randomKey;
        System.out.println(MD5Util.encrypt(text.getBytes()));
    }
}
