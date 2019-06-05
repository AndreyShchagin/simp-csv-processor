package com.symp.csv.processors

import com.symp.csv.writers.SympBufferedWriter
import org.junit.Assert
import org.junit.Test

import org.mockito.Mockito
import java.io.BufferedWriter
import java.io.File
import java.io.StringWriter
import java.net.Socket
import org.hamcrest.Matchers.*

class InboundCsvProcessorTest {
    @Test
    fun `should generate 2 files`() {
        val clientSocket = Mockito.mock(Socket::class.java)
        val buf = StringWriter()

        Mockito.`when`(clientSocket.getInputStream()).thenReturn(File("src/test/resource/test.csv").inputStream())

        InboundCsvProcessor(clientSocket, SympBufferedWriter { BufferedWriter(buf) }, 5).process()
        val result = buf.toString().split("\n")
        Assert.assertThat(result.size, `is` (10))
        Assert.assertThat(result[0], `is`("30212888860247708058"))
        Assert.assertThat(result[1], `is`("3"))
        Assert.assertTrue( "5fac6dc8-ea26-3762-8575-f279fe5e5f51,0.7626710614484215,1005421520" in result)
        Assert.assertTrue( "0977dca4-9906-3171-bcec-87ec0df9d745,0.6794981485066369,1851028776" in result)
        Assert.assertTrue( "4d968baa-fe56-3ba0-b142-be9f457c9ff4,0.6532229483547558,1403876285" in result)
        Assert.assertThat(result[5], `is`("25493180386520262311"))
        Assert.assertThat(result[6], `is`("2"))
        Assert.assertTrue("023316ec-c4a6-3e88-a2f3-1ad398172ada,0.3196604691859787,1579431460" in result)
        Assert.assertTrue("0977dca4-9906-3171-bcec-87ec0df9d745,0.50374610727888,280709214" in result)
    }

    @Test
    fun `should generate 1 file`() {
        val clientSocket = Mockito.mock(Socket::class.java)
        val buf = StringWriter()

        Mockito.`when`(clientSocket.getInputStream()).thenReturn(File("src/test/resource/test.csv").inputStream())

        InboundCsvProcessor(clientSocket, SympBufferedWriter { BufferedWriter(buf) }, 10).process()
        val result = buf.toString().split("\n")
        Assert.assertThat(result.size, `is` (7))
        Assert.assertThat(result[0], `is`("55706069246767970369"))
        Assert.assertThat(result[1], `is`("4"))
        Assert.assertTrue( "023316ec-c4a6-3e88-a2f3-1ad398172ada,0.3196604691859787,1579431460" in result)
        Assert.assertTrue( "5fac6dc8-ea26-3762-8575-f279fe5e5f51,0.7626710614484215,1005421520" in result)
        Assert.assertTrue( "0977dca4-9906-3171-bcec-87ec0df9d745,0.5916221278927586,280709214" in result)
        Assert.assertTrue( "4d968baa-fe56-3ba0-b142-be9f457c9ff4,0.6532229483547558,1403876285" in result)
    }

}
