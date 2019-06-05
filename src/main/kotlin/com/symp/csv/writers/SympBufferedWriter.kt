package com.symp.csv.writers

import com.symp.csv.model.User
import java.io.BufferedWriter
import java.math.BigInteger
import java.util.concurrent.ForkJoinPool
import java.util.stream.Collectors
import kotlin.concurrent.thread

class SympBufferedWriter(private val writerFactory: ()->BufferedWriter) : SympWriter<User, BigInteger> {

    override fun writeReport(uniqueUsersMap: Map<String, MutableList<User>>, point5: BigInteger) {
        val writer = writerFactory.invoke()
        writer.write("$point5\n")
        writer.write("${uniqueUsersMap.keys.size}\n")

        val customThreadPool = ForkJoinPool(Runtime.getRuntime().availableProcessors())
        uniqueUsersMap.keys.stream().forEach { k ->
            run {
                val avg = customThreadPool.submit<Double> { uniqueUsersMap[k]?.parallelStream()?.map { u -> u.p3 }?.collect(Collectors.averagingDouble { n -> n.toDouble() }) }.get()
                val userEntity = uniqueUsersMap[k]
                writer.write("$k,")
                writer.write("$avg,")
                writer.write("${userEntity!!.last().p4}\n")
            }
        }
        writer.flush()
        writer.close()
    }

}