package com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.apache.commons.io.IOUtils;

/** 序列化元素 */
public class SerializableElement implements Serializable {
    private static final long serialVersionUID = 1L;
    private Element element;

    public SerializableElement(final Element element){
        super();
        this.element = element;
    }

    public static long getSerialVersionUID(){
        return serialVersionUID;
    }

    public Element getElement(){
        return element;
    }
    /** 序列化实例 */
    private void writeObject(final ObjectOutputStream outputStream) throws IOException{
        outputStream.write(this.element.toBytes());
    }
    /** 解序列化 */
    private void readObject(final ObjectInputStream inputStream) throws IOException{
        //双线性对
        Pairing pairing = PairingFactory.getPairing("param/curves/a.properties");
        byte[] array = IOUtils.toByteArray(inputStream);
        if(array.length == 20){
            this.element = pairing.getZr().newRandomElement();
        }else if(array.length == 128){
            this.element = pairing.getG1().newRandomElement();
        }else{
            throw new IOException("Serialized error!");
        }
        this.element.setFromBytes(array);
    }

    @Override
    public String toString(){
        return this.element.toString();
    }
}
