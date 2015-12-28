@file:JvmName("Middleware")

package org.treeweb.routing

import org.apache.http.HttpResponse

abstract class Middleware
{
    abstract val name: String

    abstract fun isApplicable() : Boolean
    abstract fun derive(event : RouteCallingEvent, args : Array<String>) : HttpResponse
}