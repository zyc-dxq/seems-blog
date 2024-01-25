package top.seems.blog.kt.untils

import cn.hutool.core.lang.Snowflake
import cn.hutool.core.util.IdUtil
import org.babyfish.jimmer.sql.meta.UserIdGenerator

class SnowIdGeneratorString : UserIdGenerator<String> {
    override fun generate(entityType: Class<*>?): String {
        return snowflake.nextIdStr()
    }

    companion object {
        private val snowflake: Snowflake = IdUtil.getSnowflake()
    }
}
