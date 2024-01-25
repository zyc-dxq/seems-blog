package top.seems.blog.kt.runtime.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoLog(val value: String, val logType: Int = -1, val operateType: Int = -1) {
}
