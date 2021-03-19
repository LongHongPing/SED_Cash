package com;

import it.unisa.dia.gas.jpbc.Element;

class SearchClient{
    byte[] stag;
    Element[][] xtoken;
    public SearchClient(byte[] stag,Element[][] xtoken){
        this.stag = stag;
        this.xtoken = xtoken;
    }
    public byte[] getStag(){
        return stag;
    }
    public Element[][] getXtoken(){
        return xtoken;
    }
}