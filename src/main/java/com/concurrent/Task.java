package com.concurrent;

import com.Item;
import java.util.ArrayList;

/** 生产、消费单位 task */
public class Task {
    private String stag;
    private ArrayList<Item> items;
    private ArrayList<String> XSets;

    public Task(String stag,ArrayList<Item> items,ArrayList<String> XSets){
        this.stag = stag;
        this.items = items;
        this.XSets = XSets;
    }

    public String getStag(){
        return stag;
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public ArrayList<String> getXSets(){
        return XSets;
    }

    @Override
    public String toString(){
        return "";
    }
}
