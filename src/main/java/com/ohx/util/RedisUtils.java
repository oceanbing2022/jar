package com.ohx.util;

import com.ohx.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;


    private static RedisUtils redisUtils ;

    @PostConstruct
    public void init(){
        redisUtils = this ;
        redisUtils.redisTemplate = this.redisTemplate ;
    }

    /**
     * redis存入数据
     * @param key 键名
     * @param value  值
     * @param time 保存时间
     * @param timeUnit  时间单位
     * */
    public static void saveValue(String key, Object value, long time, TimeUnit timeUnit){
        redisUtils.redisTemplate.opsForValue().set(key,value,time,timeUnit);
    }

    /**
     * 获取redis中的值
     * @param key 键名
     * */
    public static <T> T getValue(String key){
        ValueOperations<String,T> valueOperations = redisUtils.redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public static Object getStrValue(String key){

        redisUtils.redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 把值作为json序列化
        redisUtils.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(User.class));

        return redisUtils.redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     * @param key 键名
     * */
    public static  boolean deleteValue(String key){
        return  redisUtils.redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return redisUtils.redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 刷新key的时间
     * @param key
     * @param time
     * @param timeUnit
     */
    public static void refresh(String key, long time, TimeUnit timeUnit){
        redisUtils.redisTemplate.expire(key,time,timeUnit);
    }
}