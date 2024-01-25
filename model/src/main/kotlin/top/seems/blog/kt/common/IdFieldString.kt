package top.seems.blog.kt.common

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass
import top.seems.blog.kt.untils.SnowIdGeneratorString

@MappedSuperclass
interface IdFieldString {
    /**
     * 主键(String)
     */
    @Id
    @GeneratedValue(generatorType = SnowIdGeneratorString::class)
    @Column(name = "id")
    val id: String
}
