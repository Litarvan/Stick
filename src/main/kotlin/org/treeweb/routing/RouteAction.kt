@file:JvmName("RouteAction")

package org.treeweb.routing

import org.apache.http.HttpResponse

abstract class RouteAction(val route: Route)
{
    abstract fun onCalled(event : RouteCallingEvent, args : Array<String>) : HttpResponse
}
