package com;

import java.io.Serializable;
import java.util.ArrayList;

/** 部分索引 */
public class XSet implements Serializable {
    ArrayList<SerializableElement> value = new ArrayList<>();
    public XSet(ArrayList<SerializableElement> value){
        this.value = value;
    }
    public ArrayList<SerializableElement> getValue(){
        return value;
    }
}
