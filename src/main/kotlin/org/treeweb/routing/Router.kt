@file:JvmName("Router")

package org.treeweb.routing

import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.nio.protocol.HttpAsyncExchange
import org.apache.http.protocol.HttpContext
import org.treeweb.WebApplication
import org.treeweb.listPackage
import java.util.*
import kotlin.collections.toTypedArray
import kotlin.text.substring
import kotlin.text.toLowerCase
import kotlin.text.trim

class Router(private val application: WebApplication)
{
    private val routes = ArrayList<Route>()
    private val middlewares = listPackage<Middleware>(application.middlewaresPackage)

    fun get(path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("GET", path, null, controllerMethod, middlewares)
    fun get(path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("GET", path, null, action, middlewares)
    fun get(name: String, path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("GET", path, name, controllerMethod, middlewares)
    fun get(name: String, path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("GET", path, name, action, middlewares)

    fun post(path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("POST", path, null, controllerMethod, middlewares)
    fun post(path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("POST", path, null, action, middlewares)
    fun post(name: String, path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("POST", path, name, controllerMethod, middlewares)
    fun post(name: String, path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("POST", path, name, action, middlewares)

    fun put(path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("PUT", path, null, controllerMethod, middlewares)
    fun put(path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("PUT", path, null, action, middlewares)
    fun put(name: String, path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("PUT", path, name, controllerMethod, middlewares)
    fun put(name: String, path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("PUT", path, name, action, middlewares)

    fun delete(path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("DELETE", path, null, controllerMethod, middlewares)
    fun delete(path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("DELETE", path, null, action, middlewares)
    fun delete(name: String, path: String, controllerMethod: String, middlewares : Array<String> = arrayOf()): Route = add("DELETE", path, name, controllerMethod, middlewares)
    fun delete(name: String, path: String, action: RouteAction, middlewares : Array<String> = arrayOf()): Route = add("DELETE", path, name, action, middlewares)

    private fun add(method: String, path: String, name: String?, controllerMethod: String, middlewares : Array<String>): Route
    {
        val route = Route(application, method, path, name, controllerMethod, filter(middlewares))
        this.routes.add(route)

        return route
    }

    private fun add(method: String, path: String, name: String?, action: RouteAction, middlewares : Array<String>): Route
    {
        val route = Route(application, method, path, name, action, filter(middlewares))
        this.routes.add(route)

        return route
    }

    fun route(route : String, request: HttpRequest, httpExchange: HttpAsyncExchange, context: HttpContext) : HttpResponse
    {
        val path : String = route.trim('/')
        val event : RouteCallingEvent = RouteCallingEvent(request, httpExchange, context)

        for (r : Route in routes)
        {
            val args : Array<String>? = r.match(path)

            if (args != null)
                return r.call(event, args)
        }

        throw RouteNotFoundException(route)
    }

    fun filter(middlewares : Array<String>) : Array<Middleware>
    {
        val list : ArrayList<Middleware> = ArrayList()

        for (middleware : String in middlewares)
        {
            var find : Boolean = false

            for (m: Middleware in this.middlewares)
                if (m.name.toLowerCase().equals(middleware.toLowerCase()))
                {
                    find = true
                    list.add(m)

                    break
                }

            if (!find)
                throw MiddlewareNotFoundException(middleware)
        }

        return list.toTypedArray()
    }

    fun getRoutes(): List<Route>
    {
        return routes
    }
}
