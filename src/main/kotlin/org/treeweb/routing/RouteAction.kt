package org.treeweb.routing

abstract class RouteAction(val route: Route)
{
    abstract fun onCalled()
}
