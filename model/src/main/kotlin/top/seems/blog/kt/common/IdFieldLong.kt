package top.seems.blog.kt.common

import com.fasterxml.jackson.annotation.JsonFormat
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass
import top.seems.blog.kt.untils.SnowIdGeneratorLong

@MappedSuperclass
interface IdFieldLong {
    /**
     * 主键(Long)
     */
    @Id
    @GeneratedValue(generatorType = SnowIdGeneratorLong::class)
    @Column(name = "id")
    @get:JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: Long
}
