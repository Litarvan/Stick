@file:JvmName("Route")

package org.treeweb.routing

import org.apache.http.HttpResponse
import org.treeweb.WebApplication
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.toTypedArray
import kotlin.text.*

class Route
{
    val application: WebApplication
    val method: String
    val path: String
    val name: String?
    val action: RouteAction
    val middlewares: Array<Middleware>

    constructor(application: WebApplication, method: String, path: String, name: String?, controllerMethod: String, middlewares : Array<Middleware>)
    {
        this.application = application
        this.method = method
        this.path = path
        this.name = name
        this.action = ControllerRouteAction(this, application, controllerMethod)
        this.middlewares = middlewares
    }

    constructor(application: WebApplication, method: String, path: String, name: String?, action: RouteAction, middlewares : Array<Middleware>)
    {
        this.application = application
        this.method = method
        this.path = path
        this.name = name
        this.action = action
        this.middlewares = middlewares
    }

    fun match(route: String): Array<String>?
    {
        val escaped = route.trim('/')
        val escapedPath = path.trim('/')
        val path = escapedPath.replace("/", "\\/").replace(Regex(":([\\w]+)"), "([^/]+)")
        val regex : Pattern = Pattern.compile("^$path$")

        if (!escaped.matches(Regex(regex.pattern())))
            return null

        val args : ArrayList<String> = ArrayList()
        val matcher : Matcher = regex.matcher(route)

        while(matcher.find())
            args.add(matcher.group(1))

        return args.toTypedArray()
    }

    fun call(event : RouteCallingEvent, args : Array<String>) : HttpResponse
    {
        for (m : Middleware in middlewares)
            if (m.isApplicable())
                return m.derive(event, args)

        return action.onCalled(event, args)
    }

}
