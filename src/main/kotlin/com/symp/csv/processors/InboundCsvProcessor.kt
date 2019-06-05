package com.symp.csv.processors

import com.symp.csv.config.DEFAULT_BATCH_SIZE
import com.symp.csv.model.User
import com.symp.csv.writers.SympWriter
import java.net.Socket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap


open class InboundCsvProcessor(override var client: Socket,
                               override var reportWriter: SympWriter<User, BigInteger>,
                               val batchSize: Int = DEFAULT_BATCH_SIZE
) : InboundProcessor<Socket, User, BigInteger> {
    override var interrupted: Boolean = false
    override val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val reader: Scanner = Scanner(client.getInputStream())
    private val uniqueUsers = HashMap<String, MutableList<User>>()
    private var cntr: Int = 1

    private val point5Sum = object : ThreadLocal<BigInteger>() {
        override fun initialValue(): BigInteger = BigInteger.ZERO
    }

    override fun process() {
        while (!interrupted && reader.hasNext()) {
            try {
                val csvLine = reader.nextLine()
                val csvRecords = csvLine.split(',')
                parseRecord(csvRecords)
                if (cntr != 0 && cntr % this.batchSize == 0) {
                    reportWriter.writeReport(uniqueUsers.toMap(), point5Sum.get())
                    uniqueUsers.clear()
                    point5Sum.set(BigInteger.ZERO)
                }
                cntr++
            } catch (ex: Exception) {
                log.error("Execution was interrupted at the runtime", ex)
                shutdown()
            }
        }
    }


    private fun parseRecord(csvRecord: List<String>) {
        //Calculate sum
        point5Sum.set(point5Sum.get().add(BigInteger(csvRecord[4])))
        val userRecord = User(csvRecord[2].toDouble(), csvRecord[3].toInt())
        if (uniqueUsers[csvRecord[0]] == null)
            uniqueUsers[csvRecord[0]] = mutableListOf(userRecord)
        else
            uniqueUsers[csvRecord[0]]?.add(userRecord)
    }

}

