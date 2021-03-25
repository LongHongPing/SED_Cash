package com.db;

import com.utils.GenUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/** redis工具测试类 */
public class RedisDbTest {
    @Test
    public void getAndReleaseResourceTest(){
        Jedis jedis = RedisDb.getJedis();
        System.out.println(jedis);
        RedisPool.releaseJedis(jedis);
    }
    @Test
    public void getRecordTest(){
        Map<String, Collection<String>> result = RedisDb.getRecords(0,100);
        for(String key : result.keySet()){
            Collection<String> fileName = result.get(key);
            System.out.println(key + " " + fileName);
        }
    }
    @Test
    public void getRecordTest2() throws IOException {
        int begin = 1024;
        while(true){
            System.out.println("Try to handle " + begin + "-" + (begin + 1000) + " data");
            File file = GenUtil.getFile("output_redis",begin + "-" + (begin + 1000) + ".txt");
            Map<String,Collection<String>> result = RedisDb.getRecords(begin,begin + 1000);
            begin += 1000;
            FileOutputStream fos = new FileOutputStream(file);
            for(String key : result.keySet()){
                Collection<String> fileName = result.get(key);
                IOUtils.write(key + " " + fileName.size() + " " + fileName + System.lineSeparator(),fos);
            }
            fos.close();
        }
    }
}
