package top.seems.blog.kt.repository.extend

import org.babyfish.jimmer.spring.repository.fetchPage
import org.babyfish.jimmer.spring.repository.orderBy
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * 扩展JRepository公共方法，需要提供一个sqlClient
 */
interface JRepositoryExtend<E : Any, ID : Any> {

    fun getSqlClient(): org.babyfish.jimmer.sql.kt.KSqlClient

    /**
     * 使用 specification超级查询语法
     */
    fun find(specification: KSpecification<E>, sort: Sort, fetcher: Fetcher<E>?): List<E> =
        getSqlClient().createQuery(specification.entityType().kotlin) {
            where(specification)
            orderBy(sort)
            select(table.fetch(fetcher))
        }.execute()

    /**
     * 使用 specification超级查询语法
     */
    fun find(specification: KSpecification<E>, pageable: Pageable, fetcher: Fetcher<E>?): Page<E> =
        getSqlClient().createQuery(specification.entityType().kotlin) {
            where(specification)
            orderBy(pageable.sort)
            select(table.fetch(fetcher))
        }.fetchPage(pageable)
}
