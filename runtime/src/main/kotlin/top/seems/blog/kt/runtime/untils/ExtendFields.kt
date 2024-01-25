package top.seems.blog.kt.runtime.untils

import org.babyfish.jimmer.kt.new
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.seems.blog.kt.model.SysUser
import top.seems.blog.kt.model.by


val traceLog: Logger = LoggerFactory.getLogger("traceLog")

/**
 * 为任何对象提供一个扩展属性-> logger
 */
val <T : Any> T.logger: Logger
    get() {
        return traceLog
    }

/**
 * 判断是否使用登录
 */
fun <T : Any> T.isHasUser(): Boolean {
    return true
}


fun <T : Any> T.currentUser(): SysUser {
    return new(SysUser::class).by {
        id = 110
        userName = "admin"
        realName = "realName"
        password = "password"
        salt = "salt"
        avatar = "avatar"
        email = "email"
        phone = "phone"
    }
}
