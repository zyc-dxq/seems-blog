package top.seems.blog.kt.model

import org.babyfish.jimmer.sql.Entity
import top.seems.blog.kt.common.DataDelField
import top.seems.blog.kt.common.HandleField
import top.seems.blog.kt.common.IdFieldLong
import top.seems.blog.kt.enums.SysLogType


@Entity
interface SysLog : IdFieldLong, HandleField, DataDelField {
    /**
     * 日志类型
     */
    val logType: SysLogType

    /**
     * 服务id
     */
    val serviceId:String?
    /**
     * 用户id
     */
    val userId: String?

    /**
     * ip
     */
    val ip: String?

    /**
     * 用户代理
     */
    val userAgent: String?

    /**
     * 请求方法
     */
    val method: String?

    /**
     * 请求地址
     */
    val requestUri: String?

    /**
     * 请求参数
     */
    val requestParams: String?

    /**
     * 耗时
     */
    val costTime: Long?
}
