package top.seems.blog.kt.model

import org.babyfish.jimmer.sql.Entity
import top.seems.blog.kt.common.DataDelField
import top.seems.blog.kt.common.HandleField
import top.seems.blog.kt.common.IdFieldLong


@Entity
interface SysUser : IdFieldLong, HandleField, DataDelField {
    /**
     * 用户名
     */
    val userName: String

    /**
     * 昵称
     */
    val realName: String

    /**
     * 密码
     */
    val password: String

    /**
     * 盐
     */
    val salt: String?

    /**
     * 头像
     */
    val avatar: String?

    /**
     * 性别
     */
    val sex: String?

    /**
     * 邮箱
     */
    val email: String?

    /**
     * 电话
     */
    val phone: String?

   /**
     * 数据状态(1正常，0冻结)
     */
    val status: Long?
}
