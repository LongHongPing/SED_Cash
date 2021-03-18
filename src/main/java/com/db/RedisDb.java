package com.db;

import redis.clients.jedis.Jedis;

import java.util.*;

/** redis工具类 */
public class RedisDb {
    /** 获取redis对象 */
    public static Jedis getJedis(){
        Jedis jedis = RedisPool.getJedis();
        if("PONG".equals(jedis.ping())){
            return jedis;
        }else{
            return null;
        }
    }
    /** 获得相应记录 */
    public static Map<String, Collection<String>> getRecords(int begin,int end){
        Jedis jedis = getJedis();
        List<String> allKeys = jedis.lrange("allKeys",begin,end-1);
        Map<String,Collection<String>> results = new HashMap<>();
        for(String key : allKeys){
            Set<String> fileNames = jedis.smembers(key);
            results.put(key,fileNames);
        }
        RedisPool.releaseJedis(jedis);
        return results;
    }
}
