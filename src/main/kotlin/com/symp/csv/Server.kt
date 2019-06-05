package com.symp.csv

import com.sun.security.ntlm.Server
import com.symp.csv.config.APP_NAME
import com.symp.csv.config.DEFAULT_BATCH_SIZE
import com.symp.csv.config.DEFAULT_SERVER_PORT
import com.symp.csv.config.RESULT_DIRRECTORY
import com.symp.csv.processors.InboundCsvProcessor
import com.symp.csv.writers.SympBufferedWriter
import org.apache.commons.cli.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.NumberFormatException
import java.net.ServerSocket
import java.util.*
import kotlin.concurrent.thread


fun main(args: Array<String>) {

    val options = Options()
            .addOption("p", "port", true, "TCP port to listen on")
            .addOption("b", "batch", false, "Batch size - default 1000")

    try {
        if (args.isEmpty())
            throw ParseException("No arguments")
        val line = DefaultParser().parse(options, args)
        val server = ServerSocket(if (line.getOptionValue("port").isNullOrBlank()) DEFAULT_SERVER_PORT else line.getOptionValue("port").toInt())
        val batchSize = if (line.getOptionValue("batch").isNullOrBlank()) DEFAULT_BATCH_SIZE else line.getOptionValue("batch").toInt()
        log.info("Server is running on port ${server.localPort} with batch size $batchSize")

        while (true) {
            val inboundConnection = server.accept()
            log.info("Inbound connection: ${inboundConnection.inetAddress.hostAddress}")
            thread { InboundCsvProcessor(inboundConnection, SympBufferedWriter { creatReportFileWriter() }).process() }
        }
    } catch (err: Exception) {
        when (err) {
            is NumberFormatException, is ParseException -> {
                HelpFormatter().printHelp(APP_NAME, options)
            }
            else -> log.error("Error when starting the server", err)
        }
    }
}

fun creatReportFileWriter(): BufferedWriter {
    val outDir = System.getProperty("user.dir")
    val reportDir = File("$outDir/$RESULT_DIRRECTORY")
    if (!reportDir.exists())
        reportDir.mkdir()
    val reportFile = File("$outDir/$RESULT_DIRRECTORY/${Date().time}.csv")
    try {
        if (!reportFile.createNewFile()) {
            log.error("Can not create a report file. Shutting down the process")
            throw InterruptedException("Can not create a file")
        }
    } catch (e: IOException) {
        log.error("Error when creating the file", e)
    }
    return reportFile.bufferedWriter()
}


val log: Logger = LoggerFactory.getLogger(Server::class.java)

