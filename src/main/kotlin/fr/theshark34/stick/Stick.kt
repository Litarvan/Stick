@file:JvmName("Stick")

package fr.theshark34.stick

import net.wytrem.logging.Logger
import net.wytrem.logging.LoggerFactory
import org.treeweb.WebApplication

class Stick : WebApplication()
{
    private final val logger : Logger = LoggerFactory.getLogger("Stick")

    override val name: String
        get() = "Stick"

    override val version: String
        get() = "1.0.0-BETA"

    override val port: Int
        get() = 24660

    override val controllersPackage: String
        get() = "fr.theshark34.stick.controllers"

    override val viewsPackage: String
        get() = "fr.theshark34.stick.views"

    override val middlewaresPackage: String
        get() = "fr.theshark34.stick.middlewares"

    override val modelsPackage: String
        get() = "fr.theshark34.stick.models"

    override fun onStart()
    {
        router.get("/hello/:name", "TestController@yolo");
    }
}
