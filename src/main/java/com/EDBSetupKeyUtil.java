package com;

import com.utils.EncUtil;
import com.utils.GenUtil;
import com.utils.SerialUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;

/** 双线性对密钥工具类 */
public class EDBSetupKeyUtil {
    private static final Pairing pairing = PairingFactory.getPairing("param/curves/a.properties");
    //随机函数F的参数
    private static final byte[] Ks = EncUtil.randomBytes(128/8);
    //Fp的参数
    private static final byte[] Ki = EncUtil.randomBytes(128/8);
    private static final byte[] Kz = EncUtil.randomBytes(128/8);
    private static final byte[] Kx = EncUtil.randomBytes(128/8);
    //群的生成元
    private static final Element g = pairing.getG1().newRandomElement();

    /** 密钥文件写入 */
    public static void serializeKey() {
        File file = GenUtil.getFile("output","allKeys.keys");
        if(file.exists()){
            System.out.println("Key file already done...");
            return;
        }
        MasterKey mk = new MasterKey(Kx,Ki,Kz,Ks,GenUtil.genKT(),new SerializableElement(g));
        SerialUtil.serialize(mk,file.getAbsolutePath());
    }
    /** 从文件读取密钥 */
    public static MasterKey deSerializeKey() {
        File file = GenUtil.getFile("output","allKeys.keys");
        //System.out.println(file.getAbsolutePath());
        MasterKey mk = SerialUtil.deSerialize(file.getAbsolutePath());
        return mk;
    }
    /** 获取密钥 */
    public static MasterKey getKey() {
        File file = GenUtil.getFile("output","allKeys.keys");
        if(file.exists()){
            return EDBSetupKeyUtil.deSerializeKey();
        }else{
            EDBSetupKeyUtil.serializeKey();
            return EDBSetupKeyUtil.deSerializeKey();
        }
    }
}
