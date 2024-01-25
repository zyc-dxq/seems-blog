package top.seems.blog.kt.repository

import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.KSqlClient
import top.seems.blog.kt.model.SysLog
import top.seems.blog.kt.repository.extend.JRepositoryExtend

interface SysLogRepository : KRepository<SysLog, Long>, JRepositoryExtend<SysLog, Long> {
    override fun getSqlClient(): KSqlClient {
        return sql
    }
}
