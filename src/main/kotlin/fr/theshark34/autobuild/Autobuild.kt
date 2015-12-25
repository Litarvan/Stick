package fr.theshark34.autobuild

import org.treeweb.WebApplication

class Autobuild : WebApplication()
{
    override val name: String
        get() = "Autobuild"

    override val version: String
        get() = "1.0.0-BETA"

    override val port: Int
        get() = 24660

    override val controllersPackage: String
        get() = "fr.Theshark34.autobuild.controllers"

    override val viewsPackage: String
        get() = "fr.Theshark34.autobuild.views"

    override val middlewaresPackage: String
        get() = "fr.Theshark34.autobuild.middlewares"

    override val modelsPackage: String
        get() = "fr.Theshark34.autobuild.models"

    override fun onStart()
    {

    }
}
