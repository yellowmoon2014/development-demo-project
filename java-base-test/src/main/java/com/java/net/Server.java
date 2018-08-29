package com.java.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {

    public static void main(String[] args) {
        start(8080);
    }

    public Object add(Map<String, Integer> map) {
        Integer version = map.get("version");
        version++;
        map.put("version", version);
        return map;
    }

    private static Object invokeSync(String className, String methodName, String paramType, Object param) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class c = Class.forName(className);
        Constructor constructor = c.getConstructor();
        Object o = constructor.newInstance();
        Method method = c.getMethod(methodName, Class.forName(paramType));
        return method.invoke(o, param);
    }

    public static void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(()-> {
                    ObjectOutputStream objectOutputStream = null;
                    ObjectInputStream objectInputStream = null;
                    try {
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        Object o = objectInputStream.readObject();
                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        Map<String, Object> map = (Map<String, Object>) o;
                        objectOutputStream.writeObject(invokeSync((String)map.get("className"), (String)map.get("methodName"), (String)map.get("paramType"), map.get("param")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
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
                                e.printStackTrace();
                            }
                        }
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
