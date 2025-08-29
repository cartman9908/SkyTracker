package com.skytracker.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            @Value("${spring.data.redis.sentinel.master}") String master,
            @Value("${spring.data.redis.sentinel.nodes}") String nodesCsv,
            @Value("${spring.data.redis.password:}") String serverPassword,
            @Value("${spring.data.redis.sentinel.password:}") String sentinelPasswordOpt
    ) {
        RedisSentinelConfiguration conf = new RedisSentinelConfiguration();
        conf.master(master);

        Arrays.stream(nodesCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(hostPort -> {
                    String[] hp = hostPort.split(":");
                    conf.sentinel(new RedisNode(hp[0], Integer.parseInt(hp[1])));
                });

        if (serverPassword != null && !serverPassword.isEmpty()) {
            conf.setPassword(RedisPassword.of(serverPassword));
        }
        if (sentinelPasswordOpt != null && !sentinelPasswordOpt.isEmpty()) {
            conf.setSentinelPassword(RedisPassword.of(sentinelPasswordOpt));
        } else if (serverPassword != null && !serverPassword.isEmpty()) {
            conf.setSentinelPassword(RedisPassword.of(serverPassword));
        }

        return new LettuceConnectionFactory(conf);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }
}
