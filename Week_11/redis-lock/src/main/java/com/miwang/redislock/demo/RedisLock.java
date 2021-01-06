package com.miwang.redislock.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * @author guozq
 * @date 2021-01-06-10:41 下午
 */
public class RedisLock {
    private enum EnumSingleton {
        /**
         * 懒汉枚举单例
         */
        INSTANCE;
        private RedisLock instance;
        
        EnumSingleton(){
            instance = new RedisLock();
        }
        public RedisLock getSingleton(){
            return instance;
        }
    }
    
    public static RedisLock getInstance(){
        return EnumSingleton.INSTANCE.getSingleton();
    }
    
    private JedisPool jedisPool = new JedisPool();
    
    /**
     * 进行加锁
     * @param lockValue lock value
     * @param seconds expire time
     * @return get lock
     */
    public boolean lock(String lockValue, int seconds) {
        try(Jedis jedis = jedisPool.getResource()) {
            return "OK".equals(jedis.set(lockValue, lockValue, "NX", "EX", seconds));
        }
    }
    
    /**
     * 释放锁
     * @param lock lock value
     * @return release lock
     */
    public boolean release(String lock) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " + "return redis.call('del',KEYS[1]) else return 0 end";
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(luaScript, Collections.singletonList(lock), Collections.singletonList(lock)).equals(1L);
        }
    }
}
