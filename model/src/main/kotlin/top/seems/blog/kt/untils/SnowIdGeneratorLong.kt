package top.seems.blog.kt.untils

import cn.hutool.core.lang.Snowflake
import cn.hutool.core.util.IdUtil
import org.babyfish.jimmer.sql.meta.UserIdGenerator

class SnowIdGeneratorLong : UserIdGenerator<Long> {
    override fun generate(entityType: Class<*>?): Long {
        return snowflake.nextId()
    }

    companion object {
        private val snowflake: Snowflake = IdUtil.getSnowflake()
    }
}
