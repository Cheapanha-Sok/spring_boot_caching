package com.example.cache.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration


@Configuration
@EnableCaching
class RedisConfiguration {

    @Bean
    fun redisSerializer(): GenericJackson2JsonRedisSerializer {
        return GenericJackson2JsonRedisSerializer()
    }

    @Bean
    fun cacheManager(
        connectionFactory: RedisConnectionFactory,
        redisSerializer: GenericJackson2JsonRedisSerializer
    ): CacheManager {
        val redisConfig: RedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .prefixCacheNameWith(this.javaClass.packageName.plus("."))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(redisConfig)
            .build()
    }
}
