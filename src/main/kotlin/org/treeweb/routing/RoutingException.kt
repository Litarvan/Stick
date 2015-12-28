@file:JvmName("RoutingException")

package org.treeweb.routing

open class RoutingException(var msg : String) : RuntimeException(msg)

class NoResponseException(val controller : String) : RoutingException("The controller '$controller' did not give a HttpResponse")
class RouteNotFoundException(val route : String) : RoutingException("Unable to find the route '$route'")
class ControllerNotFoundException(val controller : String) : RoutingException("Can't find the controller '$controller'")
class MiddlewareNotFoundException(val middleware : String) : RoutingException("Can't find the middleware '$middleware'")
class ControllerMethodNotFoundException(val controller : String, val method : String) : RoutingException("Can't find the method '$method' in the controller '$controller'")
class MethodNotSupportedException(val method : String) : RoutingException("Unsupported method '$method'")
