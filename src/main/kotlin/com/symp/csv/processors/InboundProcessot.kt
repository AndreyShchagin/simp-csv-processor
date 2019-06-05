package com.symp.csv.processors

import org.slf4j.Logger
import com.symp.csv.writers.SympWriter as SympWriter1

/**
 * The generic interface for online processors
 */
interface InboundProcessor<T:java.io.Closeable, V, K> {
    var client: T
    var reportWriter: SympWriter1<V,K>
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
