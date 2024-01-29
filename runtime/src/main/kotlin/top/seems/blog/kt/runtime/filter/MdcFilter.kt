package top.seems.blog.kt.runtime.filter

import org.slf4j.MDC
import org.springframework.stereotype.Component
import top.seems.blog.kt.runtime.untils.MessageDigestUtil
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 定义MDC过滤器，实现链路追踪
 */
@Component
class MdcFilter() : Filter {
    val requestIdTemplate: String = "traceId"
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val httpResponse: HttpServletResponse = response as HttpServletResponse
        var requestId: String? = httpRequest.getHeader(requestIdTemplate)
        if (requestId == null) {
            requestId = MessageDigestUtil.signByHttpHeader(httpRequest)
        }
        val ip: String? = getRequestId(httpRequest)
        val path: String? = httpRequest.requestURI
        MDC.put(requestIdTemplate, requestId)
        MDC.put("IP", ip)
        MDC.put("path", path)
        httpResponse.setHeader(requestIdTemplate, requestId)
        try {
            chain?.doFilter(request, response)
        } catch (e: Exception) {
            throw e
        } finally {
            MDC.clear()
        }
    }

    fun getRequestId(httpRequest: HttpServletRequest): String? {
        var ip: String? = httpRequest.getHeader("HTTP_CLIENT_IP")

        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.getHeader("HTTP_X_FORWARDED_FOR")
        }

        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.getHeader("HTTP_X_FORWARDED")
        }

        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.getHeader("HTTP_FORWARDED")
        }

        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.getHeader("HTTP_FORWARDED-FOR")
        }

        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.getHeader("Proxy-Client-IP")
        }

        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.equals("unknown", true)) {
            ip = httpRequest.remoteAddr
        }
        return ip
    }
}
