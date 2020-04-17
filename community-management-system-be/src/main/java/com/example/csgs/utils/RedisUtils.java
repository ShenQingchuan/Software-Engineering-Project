package com.example.csgs.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private final StringRedisTemplate redisTemplate;

    private static RedisUtils redisUtils;

    public RedisUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 此方法只在 Spring启动时 加载一次
     */
    @PostConstruct
    public void init() {
        redisUtils = this;
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * redis存入数据和设置缓存时间
     *
     * @param key   键
     * @param value 值
     * @param time  秒
     */
    public void set(String key, String value, long time) {
        redisUtils.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * redis根据key删除值
     * @param key   键
     */
    public void remove(String key) {
        redisUtils.redisTemplate.delete(key);
    }

    /**
     * redis存入数据
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        redisUtils.redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisUtils.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisUtils.redisTemplate.hasKey(key);
    }

    /**
     * 根据 key 设置过期时间
     *
     * @param key  key
     * @param time 秒
     * @return
     */
    public Boolean expire(String key, long time) {
        return redisUtils.redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }
}