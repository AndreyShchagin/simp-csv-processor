package com.symp.csv.processors

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class InboundCsvProcessor(override var client: Socket): InboundProcessor<Socket>{
    override var interrupted: Boolean = false
    override val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()

    override fun process() {
        while (!interrupted) {
            try {
                val text = reader.nextLine()
                val values = text.split(' ')
                write("")
            } catch (ex: Exception) {
                log.error("Execution was interrupted at the runtime")
                shutdown()
            }
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}

