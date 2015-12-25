package org.treeweb.server

import org.apache.http.ExceptionLogger
import org.apache.http.impl.nio.bootstrap.HttpServer
import org.apache.http.impl.nio.bootstrap.ServerBootstrap
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.http.ssl.SSLContexts
import java.io.File
import javax.net.ssl.SSLContext

class TreeHttpServer(val port: Int)
{
    private lateinit var bootstrap: ServerBootstrap
    lateinit var server: HttpServer

    fun init(): Unit
    {
        val config: IOReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build()

        this.bootstrap = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .setServerInfo("Treeweb Server/1.0.0-BETA")
                .setIOReactorConfig(config)
                .setExceptionLogger(ExceptionLogger.STD_ERR)
    }

    fun enableSSL(keystore: File, secret: CharArray): Unit
    {
        val context: SSLContext = SSLContexts.custom()
                .loadKeyMaterial(keystore.toURI().toURL(), secret, secret)
                .build();

        bootstrap.setSslContext(context);
    }

    public fun getBootstrap(): ServerBootstrap
            = this.bootstrap
}