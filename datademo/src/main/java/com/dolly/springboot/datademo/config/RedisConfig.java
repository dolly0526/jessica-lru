package com.dolly.springboot.datademo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * @author yusenyang
 * @create 2020/11/10 15:56
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

//        new Jackson2JsonRedisSerializer<>()

        // 配置具体的序列化方式
//        template.setKeySerializer();

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
