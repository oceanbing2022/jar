package com.ohx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * 重新配置RedisTemplate的序列化方式，
 * 默认的序列化方式会使redis服务器中的key的值乱码  如 \xac\xed\x00\x05t\x00\x02aa
 */
@Configuration
public class RedisConfig {



    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        //单独设置k的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置其他的k-v的默认的序列化
        redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer(Object.class));

        return redisTemplate;
    }
    
}
