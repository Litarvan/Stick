@file:JvmName("RouteCallingEvent")

package org.treeweb.routing

import org.apache.http.HttpRequest
import org.apache.http.nio.protocol.HttpAsyncExchange
import org.apache.http.protocol.HttpContext

class RouteCallingEvent(val request: HttpRequest, val httpExchange: HttpAsyncExchange, val context: HttpContext)