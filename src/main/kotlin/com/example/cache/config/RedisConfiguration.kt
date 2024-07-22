package com.example.cache.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.time.Duration


@Configuration
@EnableCaching
class RedisConfiguration {
    @Value("\${spring.data.redis.host}")
    val host: String? = null

    @Value("\${spring.data.redis.port}")
    val port: Int? = null

    @Value("\${spring.cache.redis.time-to-live}")
    val timeToLive: Long? = null

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host!!, port!!))
    }

    @Bean
    fun redisSerializer(): GenericJackson2JsonRedisSerializer {
        return GenericJackson2JsonRedisSerializer()
    }

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory, redisSerializer: RedisSerializer<Any>): CacheManager {
        val default: RedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(timeToLive!!))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(default)
            .build()
    }
}
