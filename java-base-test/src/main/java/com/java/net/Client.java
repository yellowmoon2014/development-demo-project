package com.java.net;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {

    public static void main(String[] args) {
        Map map = new HashMap<>(Map.of("version", 0));
//        System.out.println(Map.class.getTypeName());
        System.out.println(invokeSync("com.java.net.Server", "add", Map.class.getTypeName(), map));
    }

    public static Object send(Object messag) {
        return send(messag, "localhost:8080");
    }

    public static Object send(Object message, String url) {
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            Socket socket = new Socket(url.split(":")[0], Integer.valueOf(url.split(":")[1]));
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(message);
            InputStream inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            Object o = objectInputStream.readObject();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static Object invokeSync(String className, String methodName, String paramType, Object param) {
        return send(new HashMap(Map.of("className", className, "methodName", methodName, "paramType", paramType, "param",param)), "localhost:8080");
    }

    public static Object invokeSync(String className, String methodName, String paramType, Object param, String url) {
        return send(new HashMap(Map.of("className", className, "methodName", methodName, "paramType", paramType, "param",param)), url);
    }
}
