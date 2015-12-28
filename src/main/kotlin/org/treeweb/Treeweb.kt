@file:JvmName("Treeweb")

package org.treeweb

import java.io.File
import java.net.URL
import java.util.*
import kotlin.collections.toTypedArray
import kotlin.text.endsWith
import kotlin.text.replace
import kotlin.text.substring

public const val NAME: String = "Treeweb Server";
public const val VERSION: String = "1.0.0-BETA";
public const val DEFAULT_TIMEOUT: Int = 15000;
public const val DEFAULT_PORT: Int = 6645;
public val SUPPORTED_METHODS : Array<String> = arrayOf("GET", "POST", "PUT", "DELETE")

public fun <T> listPackage(packageName: String) : ArrayList<T>
{
    val classes : Array<Class<*>> = getClasses(packageName)
    val objects : ArrayList<T> = ArrayList()

    for (cl : Class<*> in classes)
        objects.add(cl.newInstance() as T)

    return objects
}

private fun getClasses(packageName : String) : Array<Class<*>>
{
    val classLoader : ClassLoader = Thread.currentThread().contextClassLoader
    val path : String = packageName.replace('.', '/')
    val resources : Enumeration<URL> = classLoader.getResources(path)
    val dirs : MutableList<File> = ArrayList()

    while (resources.hasMoreElements())
    {
        val resource : URL = resources.nextElement()
        dirs.add(File(resource.file))
    }

    val classes : ArrayList<Class<*>> = ArrayList()
    for (directory : File in dirs)
        classes.addAll(findClasses(directory, packageName));

    return classes.toTypedArray()
}


private fun findClasses(directory : File, packageName : String) : List<Class<*>>
{
    val classes : MutableList<Class<*>> = ArrayList()
    if (!directory.exists())
        return classes

    val files : Array<File> = directory.listFiles()
    for (file : File in files)
        if (file.isDirectory)
            classes.addAll(findClasses(file, packageName + "." + file.name))
        else if (file.name.endsWith(".class"))
            classes.add(Class.forName(packageName + '.' + file.name.substring(0, file.name.length - 6)))

    return classes
}