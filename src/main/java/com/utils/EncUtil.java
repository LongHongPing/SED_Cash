package com.utils;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/** 加密工具类 */
public class EncUtil {
     static {
         Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
     }
     private EncUtil(){}
     /** 密钥生成 */
     public static byte[] genKey(String pass,byte[] salt,int count,int keySize)
        throws InvalidKeySpecException, NoSuchAlgorithmException {
         SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
         KeySpec keySpec = new PBEKeySpec(pass.toCharArray(),salt,count,keySize);
         SecretKey tmpKey = factory.generateSecret(keySpec);
         SecretKey secretKey = new SecretKeySpec(tmpKey.getEncoded(),"AES");
         return secretKey.getEncoded();
     }
     /** CMAC-AES消息认证算法 */
     public static byte[] genCmac(byte[] key,String msg) throws UnsupportedEncodingException{
         CMac cMac = new CMac(new AESFastEngine());
         byte[] data = msg.getBytes("utf-8");

         byte [] output = new byte[cMac.getMacSize()];
         cMac.init(new KeyParameter(key));
         cMac.reset();
         cMac.update(data,0,data.length);
         cMac.doFinal(output,0);
         return output;
     }
     /** Hmac签名算法 */
     public static byte[] genHmac(byte[] key,String msg) throws UnsupportedEncodingException{
         HMac hMac = new HMac(new SHA256Digest());
         byte[] result = new byte[hMac.getMacSize()];

         byte[] msgArr = msg.getBytes("utf-8");
         hMac.init(new KeyParameter(key));
         hMac.reset();
         hMac.update(msgArr,0,msgArr.length);
         hMac.doFinal(result,0);
         return result;
     }
     /** 生成随机字节数组 */
     public static byte[] randomBytes(int size){
         byte[] salt = new byte[size];
         ThreadedSeedGenerator threadedSeedGenerator = new ThreadedSeedGenerator();
         SecureRandom random = new SecureRandom();
         random.setSeed(threadedSeedGenerator.generateSeed(20,true));
         random.nextBytes(salt);
         return salt;
     }
     /** CTR模式的AES加密 */
     public static byte[] encAES_CTR(byte[] key,byte[] ivBytes,String ident,int size)
        throws InvalidKeyException, InvalidAlgorithmParameterException,NoSuchAlgorithmException,
             NoSuchProviderException, NoSuchPaddingException, IOException {
         ident = ident + "\t\t\t";
         byte[] input = concat(ident.getBytes(),new byte[size-ident.getBytes().length]);

         IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
         SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
         Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding","BC");

         return encAES(cipher,secretKeySpec,ivParameterSpec,ivBytes,input);
     }
     /** CBC模式的AES加密 */
     public static byte[] encAES_CBC(byte[] key,byte[] ivBytes,byte[] input)
            throws InvalidKeyException,InvalidAlgorithmParameterException,NoSuchAlgorithmException,
             NoSuchProviderException,NoSuchPaddingException,IOException{
         IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
         SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

         return encAES(cipher,secretKeySpec,ivParameterSpec,ivBytes,input);

     }
     /** 加密模块 */
     private static byte[] encAES(Cipher cipher,SecretKeySpec secretKeySpec,IvParameterSpec ivParameterSpec,byte[] ivBytes,byte[] input)
             throws InvalidKeyException,InvalidAlgorithmParameterException,IOException{
         cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
         ByteArrayInputStream bis = new ByteArrayInputStream(input);
         CipherInputStream cis = new CipherInputStream(bis,cipher);
         ByteArrayOutputStream bos = new ByteArrayOutputStream();

         int ch;
         while((ch = cis.read()) > 0){
             bos.write(ch);
         }
         return concat(ivBytes,bos.toByteArray());
     }
     /** 拼接字节数组 */
    private static byte[] concat(byte[] a,byte[] b){
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen+bLen];
        System.arraycopy(a,0,c,0,aLen);
        System.arraycopy(b,0,c,aLen,bLen);
        return c;
    }
    /** CBC/CTR模式的AES解密 */
    public static byte[] deAes(byte[] input,byte[] key,String mode)
        throws InvalidKeyException,InvalidAlgorithmParameterException,NoSuchAlgorithmException,
            NoSuchProviderException,NoSuchPaddingException,IOException{
        byte[] ivBytes = new byte[16];
        byte[] cipherText = new byte[input.length-16];

        System.arraycopy(input,0,ivBytes,0,ivBytes.length);
        System.arraycopy(input,ivBytes.length,cipherText,0,cipherText.length);

        Cipher cipher = null;
        if(mode == "CBC"){
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
        }else{
            cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        }
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherOutputStream cos = new CipherOutputStream(bos,cipher);

        cos.write(cipherText);
        cos.close();
        return bos.toByteArray();
    }

}
