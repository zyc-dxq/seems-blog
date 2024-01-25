package top.seems.blog.kt.runtime.cache

import org.babyfish.jimmer.sql.kt.cfg.KCustomizer
import org.babyfish.jimmer.sql.kt.cfg.KSqlClientDsl
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ConditionalOnProperty(
    name = ["spring.profiles.active"],
    havingValue = "debezium"
)
@Component
class DebeziumCustomizer : KCustomizer {

    override fun customize(dsl: KSqlClientDsl) {

        dsl.setBinLogPropReader(
            LocalDateTime::class
        ) { _, jsonNode ->
            Instant.ofEpochMilli(
                jsonNode.asLong() / 1000
            ).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }
}

