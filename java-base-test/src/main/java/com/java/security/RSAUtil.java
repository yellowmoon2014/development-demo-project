package com.java.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * 非对称加密 test
 */
public class RSAUtil {

    public static class StringKeyPair {

        private KeyPair keyPair;

        public StringKeyPair(KeyPair keyPair) {
            this.keyPair = keyPair;
        }

        public String getPublicKey() {
            return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        }

        public String getPrivateKey() {
            return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        }
    }

    /**
     * 生成RSA密钥对
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static StringKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2046);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new StringKeyPair(keyPair);
    }

    /**
     * 公钥加密
     * @param message
     * @param publicKey
     * @return
     */
    public static String encrypt(byte[] message, String publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getRSAPublicKey(publicKey));
        byte[] bytes = cipher.doFinal(message);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static RSAPublicKey getRSAPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] buf = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(buf);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(x509KeySpec);
    }

    private static RSAPrivateKey getRSAPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] bytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytes);
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 密文解密
     * @param cipherText
     * @param privateKey
     * @return
     */
    public static byte[] decrypt(String cipherText, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getRSAPrivateKey(privateKey));
        return cipher.doFinal(Base64.getDecoder().decode(cipherText));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
        StringKeyPair keyPair = generateKeyPair();
        System.out.println(keyPair.getPublicKey());
        System.out.println(keyPair.getPrivateKey());
        int random = new Random().nextInt(1000);
        System.out.println(random);
        String cipherText = encrypt(intToBytes(random), keyPair.getPublicKey());
        System.out.println(cipherText);
        byte[] bytes = decrypt(cipherText, keyPair.getPrivateKey());
        System.out.println(bytesToInt(bytes));
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组
     */
    private static byte[] intToBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);
        result[1] = (byte)((num >>> 16)& 0xff );
        result[2] = (byte)((num >>> 8) & 0xff );
        result[3] = (byte)((num >>> 0) & 0xff );
        return result;
    }

    private static int bytesToInt(byte[] bytes) {
        int a = (bytes[0] & 0xff) << 24;
        int b = (bytes[1] & 0xff) << 16;
        int c = (bytes[2] & 0xff) << 8;
        int d = (bytes[3] & 0xff);
        return  a | b | c | d;
    }
}
