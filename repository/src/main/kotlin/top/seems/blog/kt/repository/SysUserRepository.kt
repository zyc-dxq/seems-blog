package top.seems.blog.kt.repository

import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.KSqlClient
import top.seems.blog.kt.model.SysUser
import top.seems.blog.kt.repository.extend.JRepositoryExtend

interface SysUserRepository : KRepository<SysUser, Long>, JRepositoryExtend<SysUser, Long> {
    override fun getSqlClient(): KSqlClient {
        return sql
    }
}
