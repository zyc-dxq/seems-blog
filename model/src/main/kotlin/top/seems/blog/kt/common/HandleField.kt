package top.seems.blog.kt.common

import com.fasterxml.jackson.annotation.JsonFormat
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface HandleField {
    /**
     * 数据创建时间
     */
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createTime: LocalDateTime

    /**
     * 数据创建人
     */
    val createBy: String

    /**
     * 数据修改时间
     */
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updateTime: LocalDateTime?

    /**
     * 数据修改人
     */
    val updateBy: String?
}
