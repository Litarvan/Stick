package org.treeweb.routing

import org.treeweb.WebApplication
import java.util.*

class Router(private val application: WebApplication)
{
    private val routes = ArrayList<Route>()

    fun get(path: String, controllerMethod: String): Route
            = add("GET", path, null, controllerMethod)

    fun get(path: String, action: RouteAction): Route
    {
        return add("GET", path, null, action)
    }

    fun get(name: String, path: String, controllerMethod: String): Route
    {
        return add("GET", path, name, controllerMethod)
    }

    fun get(name: String, path: String, action: RouteAction): Route
    {
        return add("GET", path, name, action)
    }

    fun post(path: String, controllerMethod: String): Route
    {
        return add("POST", path, null, controllerMethod)
    }

    fun post(path: String, action: RouteAction): Route
    {
        return add("POST", path, null, action)
    }

    fun post(name: String, path: String, controllerMethod: String): Route
    {
        return add("POST", path, name, controllerMethod)
    }

    fun post(name: String, path: String, action: RouteAction): Route
    {
        return add("POST", path, name, action)
    }

    fun add(method: String, path: String, name: String?, controllerMethod: String): Route
    {
        val route = Route(application, method, path, name, controllerMethod)
        this.routes.add(route)

        return route
    }

    fun add(method: String, path: String, name: String?, action: RouteAction): Route
    {
        val route = Route(application, method, path, name, action)
        this.routes.add(route)

        return route
    }

    fun getRoutes(): List<Route>
    {
        return routes
    }
}
