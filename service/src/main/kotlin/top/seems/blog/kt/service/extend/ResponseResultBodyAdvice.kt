package top.seems.blog.kt.service.extend

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import top.seems.blog.kt.common.Result

@RestControllerAdvice
class ResponseResultBodyAdvice : ResponseBodyAdvice<Any> {
    /**
     * 判断类或者方法是否使用了 @ResponseResultBody
     */
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        if(body is Result<*>){
            return body
        }
        return Result.OK(body)
    }
}
