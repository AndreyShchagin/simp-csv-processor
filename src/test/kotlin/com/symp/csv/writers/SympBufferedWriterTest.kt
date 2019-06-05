package com.symp.csv.writers

import com.symp.csv.model.User
import org.hamcrest.Matchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.io.BufferedWriter
import java.io.StringWriter
import java.math.BigInteger

class SympBufferedWriterTest {

    val uniqueUsers = HashMap<String, MutableList<User>>()

    @Before
    fun setup(){
        uniqueUsers["0977dca4-9906-3171-bcec-87ec0df9d745"] = mutableListOf(User(p3 = 0.18715484122922377, p4 = 982761284), User(p3 = 0.9655429720343038, p4 = 237475359))
        uniqueUsers["5fac6dc8-ea26-3762-8575-f279fe5e5f51"] = mutableListOf(User(p3 = 0.7626710614484215, p4 = 1005421520))
    }


    @Test
    fun `should parse a record`() {

        val buf = StringWriter()
        val bufWriter = SympBufferedWriter({ BufferedWriter(buf) })
        bufWriter.writeReport(uniqueUsers, BigInteger.valueOf(555777))
        val result = buf.toString().split("\n")
        assertThat(result.size, `is` (5))
        assertThat(result[0], `is`("555777"))
        assertThat(result[1], `is`("2"))
        assertTrue( "5fac6dc8-ea26-3762-8575-f279fe5e5f51" in result[2])
        assertTrue( "0.7626710614484215" in result[2])
        assertTrue( "1005421520" in result[2])
        assertTrue( "0977dca4-9906-3171-bcec-87ec0df9d745" in result[3])
        assertTrue( "0.5763489066317637" in result[3])
        assertTrue( "237475359" in result[3])
    }

    @Test(expected = NoSuchElementException::class)
    fun `should fail parsing`() {
        uniqueUsers["5fac6dc8-ea26-3762-8575-f279fe5e5f52"] = mutableListOf()
        val buf = StringWriter()
        val bufWriter = SympBufferedWriter { BufferedWriter(buf) }
        bufWriter.writeReport(uniqueUsers, BigInteger.valueOf(555777))
    }



}