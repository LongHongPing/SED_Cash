package com.db;

import com.utils.GenUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

/** mysql测试类 */
public class MysqlTest {
    private static Connection conn;
    private PreparedStatement preparedStatement;

    @Test
    public void saveTest() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/vsse";
        String user = "root";
        String password = "root";
        conn = DriverManager.getConnection(url,user,password);
        System.out.println("Database connection succeed");

        String TSetSql = "insert into TSets(stag,t) value(?,?)";
        byte[] byts = "world".getBytes("utf-8");
        ArrayList<String> fileName = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            fileName.add("index" + i);
        }
        byte[] indexes = GenUtil.obj2Byte(fileName);

        preparedStatement = conn.prepareStatement(TSetSql);
       // preparedStatement.setBytes(1,byts);
       // preparedStatement.setBytes(2,indexes);
        preparedStatement.setString(1,Arrays.toString(byts));
        preparedStatement.setString(2,Arrays.toString(indexes));
        preparedStatement.execute();

        if(preparedStatement != null){
            preparedStatement.close();
        }
    }
    @Test
    public void getTest() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/vsse";
        String user = "root";
        String password = "root";
        conn = DriverManager.getConnection(url,user,password);
        System.out.println("Database connection succeed");

        String TSetSql = "select * from TSets";
        preparedStatement = conn.prepareStatement(TSetSql);

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            /*
            byte[] b1 = resultSet.getBytes(1);
            byte[] byts = "world".getBytes("utf-8");
            System.out.println(Arrays.toString(b1));
            System.out.println(Arrays.toString(byts));
            byte[] b2 = resultSet.getBytes(2);
            ArrayList<String> indexes = (ArrayList<String>) GenUtil.byte2Obj(b2);
            System.out.println(indexes.toString());
            */;
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
        }
    }
}
