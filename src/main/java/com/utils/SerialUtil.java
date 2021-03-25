package com.utils;

import java.io.*;

/** 序列化工具类 */
public class SerialUtil {
    public static <T> void serialize(final T objectToSerial,final String fileName){
        if(fileName == null){
            throw new IllegalArgumentException("Filename cannot null!");
        }
        if(objectToSerial == null){
            throw new IllegalArgumentException("ObjectToSerial cannot null!");
        }
        try(FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream((fos))){
            oos.writeObject(objectToSerial);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static <T> T deSerialize(final String fileToDeserial){
         if(fileToDeserial == null){
             throw new IllegalArgumentException("Cannot deserialize file");
         }

         T objectOut = null;
         try(FileInputStream fis = new FileInputStream(fileToDeserial);
             ObjectInputStream ois = new ObjectInputStream(fis)){
             objectOut = (T) ois.readObject();
             //System.out.println("objectOut " + objectOut);
         }catch(IOException | ClassNotFoundException e){
             e.printStackTrace();
        }
         return objectOut;
    }
}
