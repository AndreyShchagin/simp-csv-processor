package com.symp.csv

import com.sun.security.ntlm.Server
import com.symp.csv.config.APP_NAME
import com.symp.csv.config.DEFAULT_SERVER_PORT
import com.symp.csv.processors.InboundCsvProcessor
import org.apache.commons.cli.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.lang.NumberFormatException
import java.net.ServerSocket
import kotlin.concurrent.thread


fun main(args: Array<String>) {

    val options = Options().addOption("p", "port", true, "TCP port to listen on")

    try {
        if (args.isEmpty())
            throw ParseException("No arguments")
        val line = DefaultParser().parse(options, args)
        val server = ServerSocket(if (line.getOptionValue("port").isNullOrBlank()) DEFAULT_SERVER_PORT else line.getOptionValue("port").toInt() )
        log.info("Server is running on port ${server.localPort}")

        while (true) {
            val inboundConnection = server.accept()
            log.info("Inbound connection: ${inboundConnection.inetAddress.hostAddress}")

            thread { InboundCsvProcessor(inboundConnection).process() }
        }
    } catch (err: Exception) {
        when(err) {
            is NumberFormatException, is ParseException ->{HelpFormatter().printHelp(APP_NAME, options)}
            else -> log.error("Error when starting the server",err)
        }
    }
}

val log: Logger = LoggerFactory.getLogger(Server::class.java)

