package top.seems.blog.kt

import org.babyfish.jimmer.client.EnableImplicitApi
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.InetAddress


@SpringBootApplication
@EnableImplicitApi
class App

fun main(args: Array<String>) {
    val application = runApplication<App>(*args)
    val env = application.environment
    val ip = InetAddress.getLocalHost().hostAddress
    val applicationName = env.getProperty("spring.application.name", "")
    val port = env.getProperty("server.port", "")
    var path = env.getProperty("server.servlet.context-path", "/")
    if (!path.startsWith("/")) path = "/$path"
    if (!path.endsWith("/")) path = "$path/"
    var uiPath = env.getProperty("jimmer.client.openapi.ui-path", "")
    if (uiPath.startsWith("/")) uiPath = uiPath.substring(1)
    var tsPath = env.getProperty("jimmer.client.ts.path", "")
    if (tsPath.startsWith("/")) tsPath = tsPath.substring(1)
    LoggerFactory.getLogger("traceLog").also {
        it.info("----------------------------------------------------------")
        it.info("\tApplication $applicationName is running!")
        it.info("\tLocal: http://localhost:$port$path")
        it.info("\tExternal: http://$ip:$port$path")
        it.info("\tdoc: http://$ip:$port$path$uiPath")
        it.info("\tts: http://$ip:$port$path$tsPath")
        it.info("----------------------------------------------------------")
    }
}
