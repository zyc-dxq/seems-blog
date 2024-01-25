package top.seems.blog.kt.runtime.cache

import com.fasterxml.jackson.databind.ObjectMapper
import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.spring.cache.CaffeineBinder
import org.babyfish.jimmer.spring.cache.RedisCaches
import org.babyfish.jimmer.spring.cache.RedisHashBinder
import org.babyfish.jimmer.spring.cache.RedisValueBinder
import org.babyfish.jimmer.sql.cache.Cache
import org.babyfish.jimmer.sql.cache.CacheFactory
import org.babyfish.jimmer.sql.cache.chain.ChainCacheBuilder
import org.babyfish.jimmer.sql.kt.cache.AbstractKCacheFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

@ConditionalOnProperty("spring.redis.host")
@Configuration
class CacheConfig {

    @Bean
    fun cacheFactory(
        connectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper
    ): CacheFactory {

        val redisTemplate = RedisCaches.cacheRedisTemplate(connectionFactory)

        return object : AbstractKCacheFactory() {

            // Id -> Object
            override fun createObjectCache(type: ImmutableType): Cache<*, *>? =
                ChainCacheBuilder<Any, Any>()
                    .add(CaffeineBinder(512, Duration.ofSeconds(1)))
                    .add(RedisValueBinder(redisTemplate, objectMapper, type, Duration.ofMinutes(10)))
                    .build()


            override fun createAssociatedIdCache(prop: ImmutableProp): Cache<*, *>? =
                createPropCache<Any, Any>(
                    filterState.isAffected(prop.targetType),
                    prop,
                    redisTemplate,
                    objectMapper,
                    Duration.ofMinutes(5)
                )


            override fun createAssociatedIdListCache(prop: ImmutableProp): Cache<*, List<*>>? =
                createPropCache<Any, List<*>>(
                    filterState.isAffected(prop.targetType), // ❻
                    prop,
                    redisTemplate,
                    objectMapper,
                    Duration.ofMinutes(5)
                )
        }
    }

    companion object {

        @JvmStatic
        private fun <K, V> createPropCache(
            isMultiView: Boolean,
            prop: ImmutableProp,
            redisTemplate: RedisTemplate<String, ByteArray>,
            objectMapper: ObjectMapper,
            redisDuration: Duration
        ): Cache<K, V> {
            if (isMultiView) {
                return ChainCacheBuilder<K, V>()
                    .add(RedisHashBinder(redisTemplate, objectMapper, prop, redisDuration))
                    .build()
            }

            return ChainCacheBuilder<K, V>()
                .add(CaffeineBinder(512, Duration.ofSeconds(1)))
                .add(RedisValueBinder(redisTemplate, objectMapper, prop, redisDuration))
                .build()
        }
    }
}
