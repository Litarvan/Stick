@file:JvmName("TreeServerBuilder")

package org.treeweb.server

import org.apache.http.ExceptionLogger
import org.apache.http.impl.nio.bootstrap.HttpServer
import org.apache.http.impl.nio.bootstrap.ServerBootstrap
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.http.ssl.SSLContexts
import org.treeweb.DEFAULT_TIMEOUT
import org.treeweb.NAME
import org.treeweb.VERSION
import java.io.File
import javax.net.ssl.SSLContext

class TreeServerBuilder(val port: Int)
{
    public var bootstrap: ServerBootstrap = ServerBootstrap.bootstrap()
        private set

    fun init(): TreeServerBuilder
    {
        val config: IOReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(DEFAULT_TIMEOUT)
                .setTcpNoDelay(true)
                .build()

        this.bootstrap = bootstrap
                .setListenerPort(port)
                .setServerInfo(NAME + "/" + VERSION)
                .setIOReactorConfig(config)
                .setExceptionLogger({ ex ->
                    ex.printStackTrace()
                })

        return this
    }

    fun enableSSL(keystore: File, secret: CharArray): TreeServerBuilder
    {
        val context: SSLContext = SSLContexts.custom()
                .loadKeyMaterial(keystore.toURI().toURL(), secret, secret)
                .build()

        bootstrap.setSslContext(context)

        return this
    }

    fun create(): HttpServer
    {
        return bootstrap.create()
    }
}