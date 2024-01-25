package top.seems.blog.kt.runtime.handler

import cn.hutool.http.HttpStatus
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.babyfish.jimmer.sql.runtime.ExecutionException
import org.babyfish.jimmer.sql.runtime.SaveErrorCode
import org.babyfish.jimmer.sql.runtime.SaveException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.NoHandlerFoundException
import top.seems.blog.kt.common.Result
import top.seems.blog.kt.runtime.untils.logger
import java.sql.DataTruncation
import java.sql.SQLException
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleException(e: NoHandlerFoundException): Result<Any> {
        logger.error(e.message, e)
        return Result.error(HttpStatus.HTTP_NOT_FOUND, "请求路径不存在")
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleException(e: DuplicateKeyException): Result<Any> {
        logger.error(e.message, e)
        return Result.error("数据库中已存在该记录")
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleException(e: HttpRequestMethodNotSupportedException): Result<Any> {
        val sb = StringBuffer()
        sb.append("不支持")
        sb.append(e.method)
        sb.append("请求方法，")
        sb.append("支持以下")
        val methods = e.supportedMethods
        if (methods != null) {
            for (str in methods) {
                sb.append(str)
                sb.append("、")
            }
        }
        logger.error(sb.toString(), e)
        return Result.error(405, sb.toString())
    }

    /**
     * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleException(e: MaxUploadSizeExceededException): Result<Any> {
        logger.error(e.message, e)
        return Result.error("文件大小超出10MB限制, 请压缩或降低文件质量! ")
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleException(e: DataIntegrityViolationException): Result<Any> {
        logger.error(e.message, e)
        return Result.error("执行数据库异常,违反了完整性例如：违反惟一约束、违反非空限制、字段内容超出长度等")
    }

    /**
     * 请求体body @RequestBody 参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(e: MethodArgumentNotValidException): Result<Any> {
        val msg = e.allErrors.stream().map { obj: ObjectError -> obj.defaultMessage }.collect(Collectors.joining())
        logger.error("操作失败，$msg", e)
        return Result.error("操作失败，$msg")
    }

    /**
     * Get请求 路径参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException::class)
    fun handleException(e: BindException): Result<Any> {
        val msg =
            e.allErrors.stream()
                .map { obj: ObjectError -> "【" + (obj as FieldError).field + ":" + obj.rejectedValue + "】" }
                .collect(Collectors.joining(","))
        logger.error("操作失败，$msg 参数校验不通过", e)
        return Result.error("操作失败，$msg 参数校验不通过")
    }

    /**
     * 处理 controller层 @RequestParam 注解缺少参数错误消息
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): Result<Any> {
        val msg = "缺少参数:" + e.parameterName + "(" + e.parameterType + ")"
        logger.error(msg, e)
        return Result.error(msg)
    }

    /**
     * 处理 controller层 @RequestParam、@NotBlank等 注解错误消息
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(e: ConstraintViolationException): Result<Any> {
        val msg = e.constraintViolations.stream().map { obj: ConstraintViolation<*> -> obj.message }
            .collect(Collectors.joining())
        logger.error("操作失败，$msg", e)
        return Result.error("操作失败，$msg")
    }

    /**
     * 参数格式异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleException(e: HttpMessageNotReadableException): Result<Any> {
        logger.error("操作失败，", e)
        if (e.message!!.contains("Required request body is missing")) {
            return Result.error("操作失败，缺少请求体参数")
        }
        if (e.message!!.contains("JSON parse error")) {
            var msg: String = "";
            // 处理字段空异常
            if (e.cause is MissingKotlinParameterException) {
                var fields = (e.cause as MissingKotlinParameterException).path
                msg = fields.stream().map { obj -> obj.fieldName }.collect(Collectors.joining(","))
                msg += "不能为空"
            }
            // 处理枚举异常
            if (e.cause is InvalidFormatException) {
                val currentValue = (e.cause as InvalidFormatException).value
                if (e.message!!.contains("not one of the values accepted for Enum class")) {
                    var fields = (e.cause as InvalidFormatException).path.stream().map { obj -> obj.fieldName }.collect(Collectors.joining(","))
                    val supportEnumTypes =
                        (e.cause as InvalidFormatException).targetType.fields.asList().stream().map { obj -> obj.name }
                            .collect(Collectors.joining(","))
                    msg = "<$fields>字段期待<$supportEnumTypes>中的一种，当前为<$currentValue>"

                } else {
                    val fields = (e.cause as InvalidFormatException).path.stream().map { obj -> obj.fieldName }.collect(Collectors.joining(","))
                    val targetType = (e.cause as InvalidFormatException).targetType.name
                    msg += "<$fields>字段类型错误，期待类型为<$targetType>，当前为<$currentValue>"
                }
            }
            return Result.error("操作失败，【$msg】")
        }
        return Result.error("操作失败，参数格式异常" + e.message)
    }

    @ExceptionHandler(HttpMessageNotWritableException::class)
    fun handleHttpMessageNotWritableException(e: HttpMessageNotWritableException): Result<Any> {
        logger.error("操作失败，", e)
        return Result.error("操作失败，序列化异常")
    }

    @ExceptionHandler(SaveException::class)
    fun handleException(e: SaveException): Result<Any> {
        val msgMap: MutableMap<String, String> = HashMap()
        msgMap[SaveErrorCode.NULL_TARGET.toString()] = "不能为空"
        msgMap[SaveErrorCode.ILLEGAL_TARGET_ID.toString()] = "字段格式非法"
        msgMap[SaveErrorCode.CANNOT_DISSOCIATE_TARGETS.toString()] = "无法设置，该属性不能为空"
        msgMap[SaveErrorCode.NO_ID_GENERATOR.toString()] = "缺少主键"
        msgMap[SaveErrorCode.ILLEGAL_ID_GENERATOR.toString()] = "主键类型非法"
        msgMap[SaveErrorCode.ILLEGAL_GENERATED_ID.toString()] = "字段类型不匹配"
        msgMap[SaveErrorCode.EMPTY_OBJECT.toString()] = "不能设置值，必须为空"
        msgMap[SaveErrorCode.NO_KEY_PROPS.toString()] = "业务主键缺少"
        msgMap[SaveErrorCode.NO_NON_ID_PROPS.toString()] = "不能插入没有任何属性的类型"
        msgMap[SaveErrorCode.NO_VERSION.toString()] = "无法更新，字段被分离"
        msgMap[SaveErrorCode.OPTIMISTIC_LOCK_ERROR.toString()] = "乐观锁失败，请重新提交"
        msgMap[SaveErrorCode.KEY_NOT_UNIQUE.toString()] = "已存在，业务主键不能重复"
        msgMap[SaveErrorCode.NEITHER_ID_NOR_KEY.toString()] = "业务主键和逻辑主键不能同时缺少"
        msgMap[SaveErrorCode.REVERSED_REMOTE_ASSOCIATION.toString()] = "属性被远程关联，无法执行save命令"
        msgMap[SaveErrorCode.LONG_REMOTE_ASSOCIATION.toString()] =
            "属性prop是远程(跨不同的微服务)关联的，但是它所关联的对象不是id-only"
        msgMap[SaveErrorCode.FAILED_REMOTE_VALIDATION.toString()] = "无法验证远程关联对象的id-only关联对象"
        msgMap[SaveErrorCode.UNSTRUCTURED_ASSOCIATION.toString()] = "属性未被@JoinSql修饰，无法执行save命令"
        var message = e.message
        val pattern = "\\[.*?\\]"

        val r = Pattern.compile(pattern)
        val m = r.matcher(message)
        if (m.find()) {
            message = m.group().replace("[", "").replace("]", "")
            message = message.substring(message.lastIndexOf(".") + 1)
        }
        var s = SaveErrorCode.NO_VERSION.toString()
        message += msgMap[e.code]
        logger.error(message, e)
        return Result.error("操作失败，$message")
    }

    @ExceptionHandler(ExecutionException::class)
    fun handleException(e: ExecutionException): Result<Any> {
        logger.error(e.message, e)
        var msg = "操作失败，sql校验异常";
        if(e.cause is SQLException){
            msg = ((e.cause) as SQLException).toString()
        }
        if(e.cause is DataTruncation){
            msg = ((e.cause) as DataTruncation).message.toString()
        }
        return Result.error(msg)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Result<Any> {
        logger.error(e.message, e)
        return Result.error("操作失败，系统处理错误")
    }
}
