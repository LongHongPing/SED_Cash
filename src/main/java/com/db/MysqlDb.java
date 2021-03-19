package com.db;

import bloomfilter.mutable.BloomFilter;
import com.Item;
import com.concurrent.Task;
import com.utils.SerialUtil;
import com.utils.GenUtil;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/** Mysql工具类 */
public class MysqlDb {
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static BloomFilter<byte[] > bloomFilter;

    static{
        File file = GenUtil.getFile("testBloomFilter","xsetBF001.bf");
        bloomFilter = SerialUtil.deserialize(file.getAbsolutePath());

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/vsse?serverTimezone=UTC";
            String user = "root";
            String password = "root";

            connection = DriverManager.getConnection(url,user,password);
            System.out.println("database connected success");
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    /** 数据插入 */
    public static void saveTask(Task task){
        String TSetSql = "insert into TSets(stag,t) value(?,?)";
        String XSetSql = "insert into XSets(xSet) value(?)";

        save(task,TSetSql,XSetSql);
    }
    /** 向指定表插入数据 */
    public static void saveTask(Task task,String tableName){
        String TSetSql = "insert into " + tableName + "(stag,t) value(?,?)";
        String XSetSql = "insert into XSets(xSet) values(?)";

        save(task,TSetSql,XSetSql);
    }
    /** 执行插入 */
    private static void save(Task task,String TSetSql,String XSetSql){
        try{
            //XSet保存
            preparedStatement = connection.prepareStatement(XSetSql);
            for(int i = 0;i < task.getXSets().size();i++){
                preparedStatement.setString(1,task.getXSets().get(i));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            //TSet保存
            preparedStatement = connection.prepareStatement(TSetSql);
            preparedStatement.setString(1,task.getStag());
            byte[] tmp = GenUtil.obj2Byte(task.getItems());
            if(tmp.length > 65 * 1024){
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                System.out.println("Item is too big");
                return;
            }
            preparedStatement.setBytes(2,tmp);
            try{
                preparedStatement.execute();
            }catch (Exception e){
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                System.out.println("Table TSets inset error");
                return;
            }
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** 获取全部数据 */
    public static ArrayList<Task> getAllTask(){
        ArrayList<Task> tasks = new ArrayList<>();
        String TSetSql = "select * from TSets";
        try{
            preparedStatement = connection.prepareStatement(TSetSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String stag = resultSet.getString(1);
                byte[] b2 = resultSet.getBytes(2);
                ArrayList<Item> items = (ArrayList<Item>) GenUtil.byte2Obj(b2);
                Task task = new Task(stag,items,null);
                tasks.add(task);
            }

            if(preparedStatement != null){
                preparedStatement.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tasks;
    }
    /** 指定关键字查询 */
    public static Task getTask(String key){
        String TSetSql = "select * from TSets where stag=(?)";
        return get(key,TSetSql);
    }
    /** 指定表、关键字查询 */
    public static Task getTask(String key,String tableName){
        String TSetSql = "select * from " + tableName + " where stag=(?) limit 1";
        return get(key,TSetSql);
    }
    /** 执行查询 */
    private static Task get(String key,String TSetSql){
        Task task = null;
        try{
            preparedStatement = connection.prepareStatement(TSetSql);
            preparedStatement.setString(1,key);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String stag = resultSet.getString(1);
                byte[] b2 = resultSet.getBytes(2);
                ArrayList<Item> items = (ArrayList<Item>) GenUtil.byte2Obj(b2);
                task = new Task(stag,items,null);
            }

            if(preparedStatement != null){
                preparedStatement.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return task;
    }
    /** 检查xset是否在表中 */
    public static boolean isExistXSet(String key) throws Exception{
        return bloomFilter.mightContain(key.getBytes("utf-8"));
    }
    /** 获得表中内容 */
    public ArrayList<String> getXSet(int offset,int rowCount){
        ArrayList<String> XSets = new ArrayList<>();
        Task task = null;
        String XSetSql = "select * from XSet limit " + offset + "," + rowCount;
        try{
            preparedStatement = connection.prepareStatement(XSetSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String XSet = resultSet.getString(1);
                XSets.add(XSet);
            }
            if(preparedStatement != null){
                preparedStatement.close();
            }
            return XSets;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
