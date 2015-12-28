@file:JvmName("RouteHandler")

package org.treeweb.routing

import org.apache.http.HttpRequest
import org.apache.http.nio.protocol.*
import org.apache.http.protocol.HttpContext
import org.treeweb.SUPPORTED_METHODS
import org.treeweb.WebApplication
import java.net.URLDecoder
import java.util.*
import kotlin.collections.contains
import kotlin.text.toUpperCase

class RouteHandler(val application : WebApplication) : HttpAsyncRequestHandler<HttpRequest>
{
    override fun processRequest(request: HttpRequest, context: HttpContext): HttpAsyncRequestConsumer<HttpRequest>
    {
        return BasicAsyncRequestConsumer()
    }

    override fun handle(request: HttpRequest, httpExchange: HttpAsyncExchange, context: HttpContext) {
        try
        {
            val method = request.requestLine.method.toUpperCase(Locale.ENGLISH)

            if (!SUPPORTED_METHODS.contains(method))
                throw MethodNotSupportedException(method)

            httpExchange.submitResponse(BasicAsyncResponseProducer(application.router.route(URLDecoder.decode(request.requestLine.uri, "UTF-8"), request, httpExchange, context)))
        }
        catch (t : Throwable)
        {
            application.handleError(t, request, httpExchange, context)
        }
    }
}