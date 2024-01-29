package top.seems.blog.kt.runtime.untils

import cn.hutool.http.Header
import org.babyfish.jimmer.kt.new
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.seems.blog.kt.model.SysUser
import top.seems.blog.kt.model.by
import java.security.MessageDigest
import javax.servlet.http.HttpServletRequest


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

object MessageDigestUtil {
    /**
     * 根据http header生成一个签名
     */
    fun signByHttpHeader(request: HttpServletRequest): String {
        val name1 = request.getHeader(Header.HOST.name)
        val name2 = request.getHeader(Header.CONNECTION.name)
        val name3 = request.getHeader(Header.PRAGMA.name)
        val name4 = request.getHeader(Header.CACHE_CONTROL.name)
        val name5 = request.getHeader(Header.USER_AGENT.name)
        val name6 = request.getHeader(Header.ACCEPT_ENCODING.name)
        val name7 = request.getHeader(Header.ACCEPT_LANGUAGE.name)
        val values = name1 + name2 + name3 + name4 + name5 + name6 + name7
        return md5(values)
    }

    fun md5(str: String): String {
        val digest: MessageDigest = MessageDigest.getInstance("MD5")
        val result = digest.digest(str.toByteArray())
        val stringBuilder = StringBuilder()

        result.forEach {
            val value = it
            val hex = value.toInt() and (0xff)
            val hexStr = Integer.toHexString(hex)
            if (hexStr.length == 1) {
                stringBuilder.append("0").append(hexStr)
            } else {
                stringBuilder.append(hexStr)
            }
        }
        return stringBuilder.toString()
    }
}
