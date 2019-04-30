package pl.edu.agh.bioauth.apigateway.util

import java.io.File
import java.util.*

object FileManager {

    private val fileDirPath: String =
            "${System.getProperty("user.home")}${File.separator}bioauth${File.separator}data${File.separator}patterns"
    private val fileDir: File
        get() {
            val dir = File(fileDirPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }

    private val tempDirPath: String = System.getProperty("java.io.tmpdir")
    private val tempDir: File = File(tempDirPath)

    private val randomFileName: String
        get() = "${UUID.randomUUID()}_${System.currentTimeMillis()}"

    fun createFile(fileName: String?): File = createFile(fileDir, fileName)

    fun createTempFile(fileName: String?): File = createFile(tempDir, fileName)

    private fun createFile(dir: File, fileName: String?) = File(dir, fileName ?: randomFileName)
}