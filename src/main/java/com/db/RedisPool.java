package com.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** redis线程池 */
public final class RedisPool {
    private static String ADDR = "";
    private static int PORT = 6379;
    private static String AUTH = "";

    private static int MAX_ACTIVE = 1024;
    private static int MAX_IDLE = 200;
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;

    private static boolean TEST = true;
    private static JedisPool jedisPool = null;

    /** 初始化redis连接池 */
    static {
        try{
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setMaxIdle(MAX_IDLE);
            config.setTestOnBorrow(TEST);

            jedisPool = new JedisPool(config,ADDR,PORT,TIMEOUT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** 获取redis实例 */
    public synchronized static Jedis getJedis(){
        try{
            if(jedisPool != null){
                Jedis jedis = jedisPool.getResource();
                return jedis;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /** 释放redis实例 */
    public static void releaseJedis(final Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
    }
}
