@file:JvmName("ControllerRouteAction")

package org.treeweb.routing

import org.apache.http.HttpResponse
import org.treeweb.WebApplication
import java.lang.reflect.Method
import kotlin.collections.dropLastWhile
import kotlin.text.isEmpty
import kotlin.text.split

class ControllerRouteAction : RouteAction
{
    var application: WebApplication
        private set
    private var controller: Class<*>
    private var method: Method

    constructor(route: Route, application: WebApplication, controllerMethod: String) : super(route)
    {
        this.application = application

        val strings = controllerMethod.split("@").dropLastWhile({ it.isEmpty() })
        if (strings.size != 2)
            throw IllegalArgumentException("Controller method need to be Controller@method")

        val controllerString = strings[0]
        val methodString = strings[1]

        val controllerPath = application.controllersPackage + "." + controllerString
        val controller: Class<*>

        try
        {
            controller = Class.forName(controllerPath)
        }
        catch (e: ClassNotFoundException)
        {
            throw ControllerNotFoundException(controllerPath)
        }

        val method: Method

        try
        {
            method = controller.getDeclaredMethod(methodString, RouteCallingEvent::class.java, Array<String>::class.java)
        }
        catch (e: NoSuchMethodException)
        {
            throw ControllerMethodNotFoundException(controllerPath, methodString)
        }

        this.controller = controller
        this.method = method
    }

    constructor(route: Route, application: WebApplication, controller: Class<Any>, method: Method) : super(route)
    {
        this.application = application
        this.controller = controller
        this.method = method
    }

    override fun onCalled(event : RouteCallingEvent, args : Array<String>) : HttpResponse
    {
        val controllerInstance = controller.newInstance()
        var result = method.invoke(controllerInstance, event, args)

        if (result is HttpResponse)
            return result
        else
            throw NoResponseException(controller.simpleName)
    }
}
