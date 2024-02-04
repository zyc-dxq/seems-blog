package top.seems.blog.kt.common

import org.springframework.http.HttpStatus
import java.io.Serializable

data class Result<T>(val code: Int, var message: String?, var result: T?) : Serializable {
    val timestamp: Long = System.currentTimeMillis()

    var success: Boolean = true

    companion object {
        fun <T> OK(): Result<T> {
            return OK("操作成功")
        }

        fun <T> OK(msg: String?): Result<T> {
            return OK(HttpStatus.OK.value(), msg)
        }

        fun <T> OK(data: T): Result<T> {
            return Result<T>(HttpStatus.OK.value(), "操作成功", data)
        }


        fun <T> OK(code: Int, msg: String?): Result<T> {
            return OK(code, msg, null)
        }

        fun <T> OK(msg: String, data: T): Result<T> {
            return Result<T>(HttpStatus.OK.value(), msg, data)
        }

        fun <T> OK(code: Int, msg: String?, data: T?): Result<T> {
            return Result<T>(code, msg, data)
        }

        fun <T> error(): Result<T> {
            return error("系统处理错误")
        }

        fun <T> error(msg: String): Result<T> {
            return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg)
        }

        fun <T> error(code: Int, msg: String): Result<T> {
            return error(code, msg, null)
        }

        fun <T> error(msg: String, data: T): Result<T> {
            return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data)
        }

        fun <T> error(code: Int, msg: String, data: T?): Result<T> {
            val res = Result<T>(code, msg, data)
            res.success = false
            return res
        }

        /**
         * 无权限访问返回结果
         */
        fun <T> noauth(msg: String): Result<T> {
            return error(HttpStatus.FORBIDDEN.value(), msg, null)
        }
    }


}
