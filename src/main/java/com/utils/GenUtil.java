package com.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 通用工具类 */
public class GenUtil {
    /** 加密初始向量 */
    private static final byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    /** 获取文件 */
    public static File getFile(String dirname,String fileName){
        String path = System.getProperty("user.dir");
        path = path + File.separator + dirname;
        File file = new File(path,fileName);
        return file;
    }
    /** PRF */
    public static byte[] F(byte[] ks, String w) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] byts = messageDigest.digest(w.getBytes("utf-8"));
        return byts;
    }
    /** 随机函数 */
    public static Element Fp(final Pairing pairing, final byte[] a1, final byte[] a2)throws Exception{
        byte[] res = F(a1,new String(a2,"utf-8"));
        return pairing.getZr().newElementFromHash(res,0,res.length);
    }
    /** 获得索引关键词 */
    public static byte[] genKT(){
        final byte[] KT = EncUtil.randomBytes(128/8);
        return KT;
    }
    /** 加密算法 */
    public static byte[] encAES(byte[] key,String plain)throws Exception{
        final byte[] cipher = EncUtil.encAES_CBC(key,ivBytes,plain.getBytes("utf-8"));
        return cipher;
    }
    /** 解密算法 */
    public static String deAES(byte[] key,byte[] cipher) throws Exception{
        byte[] plain = EncUtil.deAes(cipher,key,"CBC");
        return new String(plain,"utf-8");
    }
    /** 将对象转化为数组 */
    public static byte[] obj2Byte(Object obj) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        return bos.toByteArray();
    }
    public static Object byte2Obj(byte[] byts){
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        try{
            bis = new ByteArrayInputStream(byts);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            if(ois != null){
                try{
                    ois.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /** 文件写入 */
    public static void write(byte[] input,String outputFile,String dirPath){
        (new File(dirPath)).mkdir();
        try{
            OutputStream os = null;
            try{
                os = new BufferedOutputStream(new FileOutputStream(dirPath + "/" + outputFile));
                os.write(input);
            }finally {
                os.close();
            }
        }catch (FileNotFoundException  e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /** 文件读取 */
    public static byte[] read(String path){
        File file = new File(path);
        byte[] result = null;
        try{
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            result = readAndClose(is);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
    private static byte[] readAndClose(InputStream is){
        byte[] bucket = new byte[32*1024];
        ByteArrayOutputStream bos = null;
        try{
            try{
                bos = new ByteArrayOutputStream(bucket.length);
                int byteRead = 0;
                while(byteRead != -1){
                    byteRead = is.read(bucket);
                    if(byteRead > 0){
                        bos.write(bucket,0,byteRead);
                    }
                }
            }finally {
                is.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    /** 二进制数组转整型数组 */
    public static int[] getBits(byte[] data,int size){
        int[] bitArray = new int[size];
        for(int i = 0;i < size;i++){
            bitArray[i] = getBit(data,i);
        }
        return bitArray;
    }
    public static int getBit(byte[] data,int pos){
        int posByte = pos / 8;
        int posBit = pos % 8;
        byte valByte = data[posByte];
        int valInt = valByte >> (8 - (posBit + 1)) & 0x0001;
        return valInt;
    }
    /** 布尔型转字符型 */
    public static String bool2Str(boolean[] msg){
        String result = "";
        for(int i = 0;i < msg.length;i++){
            if(msg[i] == true){
                result = result + 1;
            }else{
                result = result + 0;
            }
        }
        return result;
    }
    /** 布尔型转二进制 */
    public static byte[] bool2Byte(boolean[] msg){
        byte[] bytes = new byte[msg.length/8];
        for(int i = 0;i < bytes.length;i++){
            for(int j = 0;j < 8;j++){
                if(msg[i*8+j]){
                    bytes[i] |= (128 >> j);
                }
            }
        }
        return bytes;
    }
    /** 二进制转布尔型 */
    public static boolean[] byte2Bool(byte[] bytes){
        boolean[] bits = new boolean[bytes.length*8];
        for(int i = 0;i < bytes.length*8;i++){
            if((bytes[i/8] & (1 << (7 - (i % 8)))) > 0){
                bits[i] = true;
            }
        }
        return bits;
    }
}
