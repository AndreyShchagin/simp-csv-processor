package com.symp.csv

import com.symp.csv.config.RESULT_DIRRECTORY
import org.junit.Assert
import org.junit.Test
import org.hamcrest.Matchers.`isA`
import org.junit.AfterClass
import java.io.BufferedWriter
import java.io.File


class ServerKtTest {

    companion object {
        @AfterClass
        @JvmStatic
        fun cleanUp() {
            println("Cleanup!")
            val outDir = System.getProperty("user.dir")
            val reportDir = File("$outDir/$RESULT_DIRRECTORY")
            reportDir.listFiles().forEach { file->file.delete() }
            reportDir.delete()
        }
    }

    @Test
    fun creatReportFileWriterTest() {
        val fileWriter = creatReportFileWriter()
        Assert.assertNotNull(fileWriter)
        Assert.assertThat(fileWriter, `isA`(BufferedWriter::class.java))
    }


}