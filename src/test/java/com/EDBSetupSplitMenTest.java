package com;

import com.concurrent.Task;
import com.db.MysqlDb;
import org.junit.Test;

import java.util.ArrayList;

public class EDBSetupSplitMenTest {
    @Test
    public void t1() throws Exception{
        String keyword = "keyword";
        ArrayList<String> fileName = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            fileName.add("index" + i);
        }
        Task task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        System.out.println(task.getStag().length());
        System.out.println(task.getStag());
        ArrayList<String> XSets = task.getXSets();
        for(String XSet : XSets){
            System.out.println(XSet.length());
            System.out.println(XSet);
        }
        task.getItems();
    }
    @Test
    public void insertItemTest() throws Exception{
        String keyword = "key1";
        ArrayList<String> fileName = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            fileName.add("index" + i);
        }
        Task task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        MysqlDb.saveTask(task,"TSets");
        /*
        keyword = "key2";
        fileName = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            fileName.add("index" + i);
        }
        task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        MysqlDb.saveTask(task,"TSets");
        */
    }
}
