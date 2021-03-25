package com.db;

import bloomfilter.CanGenerateHashFrom;
import bloomfilter.mutable.BloomFilter;
import com.*;
import com.concurrent.Task;
import com.utils.GenUtil;
import com.utils.SerialUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/** mysql工具测试类 */
public class MysqlDbTest {
    @Test
    public void connectionTest(){
        MysqlDb mysqlDb = new MysqlDb();
    }
    @Test
    public void saveTaskTest() throws Exception{
        String keyword = "text1";
        ArrayList<String> fileName = new ArrayList<>();
        fileName.add("index1");
        fileName.add("index2");
        Task task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        ArrayList<Item> items = task.getItems();
        for(Item item : items){
            System.out.println(item.toString());
        }
        MysqlDb mysqlDb = new MysqlDb();
        mysqlDb.saveTask(task);
    }
    @Test
    public void saveTaskTest2() throws Exception{
        String keyword = "key1";
        ArrayList<String> fileName = new ArrayList<>();
        fileName.add("index1");
        fileName.add("index2");
        fileName.add("index3");
        Task task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        MysqlDb mysqlDb = new MysqlDb();
        mysqlDb.saveTask(task);

        keyword = "key2";
        fileName = new ArrayList<>();
        fileName.add("index1");
        fileName.add("index2");
        task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        mysqlDb = new MysqlDb();
        mysqlDb.saveTask(task);

        keyword = "key3";
        fileName = new ArrayList<>();
        fileName.add("index1");
        task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        mysqlDb = new MysqlDb();
        mysqlDb.saveTask(task);
    }
    @Test
    public void getAllTaskTest() throws Exception{
        MysqlDb mysqlDb = new MysqlDb();
        ArrayList<Task> tasks = mysqlDb.getAllTask();
        byte[] key1 = TSetSplitMem.TSetGetTag("key1");
        byte[] key2 = TSetSplitMem.TSetGetTag("key2");
        System.out.println(Arrays.toString(key1));
        System.out.println(Arrays.toString(key2));
        for(Task task : tasks){
            System.out.println(task.getStag());
        }
    }
    @Test
    public void getTask() throws Exception{
        byte[] key1 = TSetSplitMem.TSetGetTag("key1");
        MysqlDb mysqlDb = new MysqlDb();
        Task task = mysqlDb.getTask(Arrays.toString(key1));
        ArrayList<Item> items = task.getItems();
        for(Item item : items){
            byte[] e = item.getE();
            MasterKey mk = EDBSetupKeyUtil.getKey();
            byte[] Ke = GenUtil.F(mk.getKs(),"key1");
            System.out.println(GenUtil.deAES(Ke,e));
        }
    }
    @Test
    public void deSerializeTest(){
        MysqlDb mysqlDb = new MysqlDb();
        ArrayList<Task> tasks = mysqlDb.getAllTask();
        for(Task task : tasks){
            System.out.println(" ===== ");
            ArrayList<Item> items = task.getItems();
            for(Item item : items){
                System.out.println(item.toString());
            }
            System.out.println(" ===== ");
        }
    }
    @Test
    public void getXSetTest(){
        MysqlDb mysqlDb = new MysqlDb();
        ArrayList<String> XSets = mysqlDb.getXSet(10,1000);
        for(String XSet : XSets){
            System.out.println(XSet);
        }
    }
    @Test
    public void getXSetTest2() throws Exception{
        MysqlDb mysqlDb = new MysqlDb();
        int offset = 0;
        long expectElement = 1000000;
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bloomFilter = BloomFilter.apply(
                expectElement,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$
        );
        File file = GenUtil.getFile("tset_bloomFilter","xset_bf_1000000.bf");
        while(offset < 1000000){
            System.out.println(offset + "~" + (offset + 1000));
            ArrayList<String> XSets = mysqlDb.getXSet(offset,1000);
            for(String XSet : XSets){
                bloomFilter.add(XSet.getBytes("utf-8"));
            }
            offset += 1000;
        }
        SerialUtil.serialize(bloomFilter,file.getAbsolutePath());
        bloomFilter.dispose();
    }
}
