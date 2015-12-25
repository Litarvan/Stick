package org.treeweb.routing

import org.treeweb.WebApplication

class Route
{
    var application: WebApplication
        private set
    var method: String
    var path: String
    var name: String?
    var action: RouteAction

    constructor(application: WebApplication, method: String, path: String, name: String?, controllerMethod: String)
    {
        this.application = application
        this.method = method
        this.path = path
        this.name = name
        this.action = ControllerRouteAction(this, application, controllerMethod)
    }

    constructor(application: WebApplication, method: String, path: String, name: String?, action: RouteAction)
    {
        this.application = application
        this.method = method
        this.path = path
        this.name = name
        this.action = action
    }
}
