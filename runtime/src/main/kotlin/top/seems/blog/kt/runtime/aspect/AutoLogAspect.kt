package top.seems.blog.kt.runtime.aspect

import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Pointcut

//@Aspect
//@Component
class AutoLogAspect {
    @Pointcut("@annotation(top.seems.blog.kt.runtime.annotation.AutoLog)")
    fun logPointCut() {
    }

    @Around("logPointCut()")
    fun around(point: org.aspectj.lang.ProceedingJoinPoint): Any {
        val beginTime = System.currentTimeMillis()
        val res = point.proceed()
        val time = System.currentTimeMillis() - beginTime
        return res
    }
}
