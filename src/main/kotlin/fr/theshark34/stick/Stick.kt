package fr.theshark34.stick

import org.treeweb.WebApplication

class Stick : WebApplication()
{
    override val name: String
        get() = "Stick"

    override val version: String
        get() = "1.0.0-BETA"

    override val port: Int
        get() = 24660

    override val controllersPackage: String
        get() = "fr.Theshark34.stick.controllers"

    override val viewsPackage: String
        get() = "fr.Theshark34.stick.views"

    override val middlewaresPackage: String
        get() = "fr.theshark34.stick.middlewares"

    override val modelsPackage: String
        get() = "fr.Theshark34.stick.models"

    override fun onStart()
    {

    }
}
