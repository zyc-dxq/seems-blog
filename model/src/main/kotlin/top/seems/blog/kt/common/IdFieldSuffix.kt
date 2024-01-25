package top.seems.blog.kt.common

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass
import top.seems.blog.kt.untils.SnowIdGeneratorString

@MappedSuperclass
interface IdFieldSuffix {
    /**
     * 主键(自定义数据库字段)
     */
    @Id
    @GeneratedValue(generatorType = SnowIdGeneratorString::class)
    @Column(name = "ID_")
    val id: String
}
