package top.seems.blog.kt.common

import org.babyfish.jimmer.sql.LogicalDeleted
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface DataDelField {
    /**
     * 逻辑删除时间
     */
    @LogicalDeleted("now")
    val deletedTime: LocalDateTime?
}
