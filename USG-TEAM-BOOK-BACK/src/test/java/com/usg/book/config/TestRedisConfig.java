package com.usg.book.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.embedded.RedisServer;

@Configuration
@Profile("test")
public class TestRedisConfig {

    private final RedisServer redisServer = new RedisServer(6379);

    @PostConstruct
    public void startRedis() {
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactoryMock() {
        return new LettuceConnectionFactory("localhost", 6379);
    }
}
