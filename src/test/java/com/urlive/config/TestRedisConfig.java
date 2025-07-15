package com.urlive.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
public class TestRedisConfig {

    @Bean
    public RedisConnectionFactory testRedisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory("localhost", 6380);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public StringRedisTemplate testStringRedisTemplate(RedisConnectionFactory testRedisConnectionFactory) {
        return new StringRedisTemplate(testRedisConnectionFactory);
    }
}
