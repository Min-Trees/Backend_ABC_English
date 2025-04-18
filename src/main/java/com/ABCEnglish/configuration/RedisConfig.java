package com.ABCEnglish.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // Log thông tin cấu hình Redis
        if (connectionFactory instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory lettuce = (LettuceConnectionFactory) connectionFactory;
            logger.info("Redis config - Host: {}, Port: {}, Database: {}",
                    lettuce.getHostName(), lettuce.getPort(), lettuce.getDatabase());
        }

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        // Kiểm tra kết nối
        try {
            String pingResponse = connectionFactory.getConnection().ping();
            logger.info("Redis PING response: {}", pingResponse);
        } catch (Exception e) {
            logger.error("Failed to connect to Redis during initialization: {}", e.getMessage(), e);
        }

        return template;
    }
}