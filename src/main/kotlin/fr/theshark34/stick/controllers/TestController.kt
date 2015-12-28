package fr.theshark34.stick.controllers

import org.apache.http.HttpResponse
import org.apache.http.nio.entity.NStringEntity
import org.treeweb.routing.RouteCallingEvent

class TestController
{
    fun yolo(event : RouteCallingEvent, args : Array<String>) : HttpResponse
    {
        val response : HttpResponse = event.httpExchange.response
        response.entity = NStringEntity("Hey ${args[0]}")

        return response
    }
}