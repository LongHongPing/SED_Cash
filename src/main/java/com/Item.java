package com;

import java.io.Serializable;
import java.util.Arrays;

/** 密文和元素 */
public class Item implements Serializable {
    //密文
    byte[] byts;
    //元素
    SerializableElement serializableElement;

    public Item(byte[] byts,SerializableElement y){
        this.byts = byts;
        this.serializableElement = serializableElement;
    }
    public byte[] getE(){
        return byts;
    }
    public SerializableElement getY(){
        return serializableElement;
    }
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("byts: " + Arrays.toString(byts));
        stringBuilder.append("serializableElement: " + serializableElement.getElement().toString());
        return stringBuilder.toString();
    }
}
