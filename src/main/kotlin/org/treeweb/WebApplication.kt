@file:JvmName("WebApplication")

package org.treeweb

import net.wytrem.logging.Logger
import net.wytrem.logging.LoggerFactory
import org.apache.http.ExceptionLogger
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.entity.ContentType
import org.apache.http.impl.nio.bootstrap.HttpServer
import org.apache.http.impl.nio.bootstrap.ServerBootstrap
import org.apache.http.nio.entity.NStringEntity
import org.apache.http.nio.protocol.BasicAsyncResponseProducer
import org.apache.http.nio.protocol.HttpAsyncExchange
import org.apache.http.protocol.HttpContext
import org.treeweb.routing.RouteHandler
import org.treeweb.routing.Router
import org.treeweb.server.TreeServerBuilder
import java.lang.reflect.Field
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

abstract class WebApplication
{
    private final val logger : Logger = LoggerFactory.getLogger("Treeweb")

    final val router = Router(this)
    protected final val bootstrap: TreeServerBuilder = TreeServerBuilder(DEFAULT_PORT);
    protected lateinit var server: HttpServer

    abstract val name: String
    abstract val version: String
    abstract val port: Int

    abstract val controllersPackage: String
    abstract val viewsPackage: String
    abstract val middlewaresPackage: String
    abstract val modelsPackage: String

    protected abstract fun onStart()

    protected open fun started()
    {
    }

    final fun start()
    {
        logger.info("Starting TreeWeb - Creating Web Server")

        bootstrap.init()
        bootstrap.bootstrap.registerHandler("*", RouteHandler(this))

        onStart()

        logger.info("Starting server")

        server = bootstrap.create()
        server.start()

        val address : InetAddress? = field("ifAddress") as InetAddress?
        val port : Int = field("port") as Int

        logger.info("Listening on " + (address ?: InetAddress.getLocalHost()).hostAddress + " (" + (address ?: InetAddress.getLocalHost()).hostName + ") " + port)

        started()

        logger.info("Loading finished")

        server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)

        Runtime.getRuntime().addShutdownHook(thread()
        {
            server.shutdown(5, TimeUnit.SECONDS)
        })
    }

    private final fun field(field : String) : Any?
    {
        val f : Field = server.javaClass.getDeclaredField(field)
        f.isAccessible = true

        return f.get(server)
    }

    fun handleError(t: Throwable, request: HttpRequest, httpExchange: HttpAsyncExchange, context: HttpContext) {
        val response: HttpResponse = httpExchange.response
        response.entity = NStringEntity(getErrorText(t, false), ContentType.TEXT_HTML)

        httpExchange.submitResponse(BasicAsyncResponseProducer(response))
    }

    private fun getErrorText(t: Throwable, cause: Boolean) : String
    {
        var str : String = if(!cause) "<b>Exception caught !</b>\n\n<br /><br />\n<pre>\n" else "<u>Caused by</u>"

        str += t.toString() + "\n"

        for (element : StackTraceElement in t.stackTrace)
            str += "    at ${element.toString()}\n"

        if (t.cause != null)
            str += getErrorText(t.cause as Throwable, true)

        if (!cause)
            str += "\n</pre>"

        return str
    }
}
