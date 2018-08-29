package com.javax.net.ssl;

import com.java.net.Server;
import com.java.security.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SSLServer {

    private static final String publicKey;

    private static final String privateKey;

    static {
        RSAUtil.StringKeyPair stringKeyPair = null;
        try {
            stringKeyPair = RSAUtil.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        publicKey = stringKeyPair.getPublicKey();
        privateKey = stringKeyPair.getPrivateKey();
    }

    private static final Map<String, String> secretKeyCache = new ConcurrentHashMap<>();

    public Object callFirst(Map<String, Object> param) {
        Map<String, Object> body = new HashMap<>(param);
        String syn = body.get("syn").toString();
        String ack = String.valueOf(Math.random());
        body.put("ack", ack);
        body.put("publicKey", publicKey);
        secretKeyCache.put("synAck", syn + ack);
        return body;
    }

    public Object callSecond(Map<String, Object> param) {
        Map<String, Object> body = new HashMap<>(param);
        String randomKeyEncrypt = body.get("randomKeyEncrypt").toString();
        try {
            byte[] bytes = RSAUtil.decrypt(randomKeyEncrypt, privateKey);
            String randomKey = new String(bytes);
            String text = secretKeyCache.get("synAck") + randomKey;
            String secretKey = getSecretKey(text);
            System.out.println(text);
            System.out.println(secretKey);
            secretKeyCache.put("secretKey",secretKey );
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSecretKey(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(text.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Server.start(8080);
    }
}
