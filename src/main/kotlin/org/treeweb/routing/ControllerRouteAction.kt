package org.treeweb.routing

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
            throw IllegalArgumentException("Can't find controller : " + controllerPath)
        }

        val method: Method

        try
        {
            method = controller.getDeclaredMethod(methodString)
        }
        catch (e: NoSuchMethodException)
        {
            throw IllegalArgumentException("Can't find method : $methodString in controller $controllerPath")
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

    override fun onCalled()
    {
        val controllerInstance = controller.newInstance()
        method.invoke(controllerInstance)
    }
}
