package com;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;
import java.util.Arrays;

/** 主密钥类 */
public class MasterKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] Kx = null;
    private byte[] Ki = null;
    private byte[] Kz = null;
    private byte[] Ks = null;
    private byte[] Kt = null;
    private SerializableElement g;

    public MasterKey(byte[] Kx,byte[] Ki,byte[] Kz,byte[] Ks,byte[] Kt,SerializableElement g){
        this.Kx = Kx;
        this.Ki = Ki;
        this.Kz = Kz;
        this.Ks = Ks;
        this.Kt = Kt;
        this.g = g;
    }
    public byte[] getKx(){
        return Kx;
    }
    public byte[] getKi(){
        return Ki;
    }
    public byte[] getKz(){
        return Kz;
    }
    public byte[] getKs(){
        return Ks;
    }
    public byte[] getKt(){
        return Kt;
    }
    public Element getG(){
        return g.getElement();
    }
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Master key message" + System.lineSeparator());
        stringBuilder.append("Kx: " + Arrays.toString(Kx));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Ki: " + Arrays.toString(Ki));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Kz: " + Arrays.toString(Kz));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Ks: " + Arrays.toString(Ks));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Kt: " + Arrays.toString(Kt));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("g: " + g.getElement().toString());
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}
