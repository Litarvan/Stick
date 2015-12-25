package org.treeweb

import org.treeweb.routing.Router

abstract class WebApplication
{
    protected final val router = Router(this)

    abstract val name: String
    abstract val version: String
    abstract val port: Int

    abstract val controllersPackage: String
    abstract val viewsPackage: String
    abstract val middlewaresPackage: String
    abstract val modelsPackage: String

    protected abstract fun onStart()

    fun start()
    {

    }
}
