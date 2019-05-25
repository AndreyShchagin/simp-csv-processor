package com.symp.csv.processors

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * The generic interface for online processors
 */
interface InboundProcessor<T:java.io.Closeable> {
    var client: T
    var interrupted: Boolean
    val log: Logger
    /**
     * Start processing
     */
    fun process()

    /**
     * Gracefully shuts down the processor
     */
    fun shutdown(){
        interrupted = true
        client.close()
        log.info("Closing the client $client")
    }
}
