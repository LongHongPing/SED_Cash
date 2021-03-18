package com;

import com.concurrent.Task;
import com.db.MysqlDb;
import com.utils.GenUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/** 索引结果集 */
public class TSetSplitMem {
    public static MasterKey mk = null;
    static{
        mk = EDBSetupKeyUtil.getKey();
    }

    private HashMap<byte[], ArrayList<Item>> T;
    public HashMap<byte[],ArrayList<Item>> getT(){
        return T;
    }

    /** 生成搜索用stag */
    public static byte[] TSetGetTag(String w) throws Exception{
        byte[] stag = GenUtil.F(mk.getKt(),w);
        return stag;
    }
    /** 搜索 */
    public static ArrayList<Item> TSetRetrive(byte[] stag,String tableName){
        MysqlDb mysqlDb = new MysqlDb();
        Task task = mysqlDb.getTask(Arrays.toString(stag),tableName);
        if(task == null){
            return null;
        }else{
            return task.getItems();
        }
    }
    public TSetSplitMem(HashMap<byte[],ArrayList<Item>> t){
        T = t;
    }
}
