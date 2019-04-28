package pl.edu.agh.bioauth.apigateway.util

import java.io.File
import java.util.*

object FileManager {

    private val fileDirPath: String = "${System.getProperty("user.home")}${File.separator}bioauth"
    private val fileDir: File
        get() {
            val dir = File(fileDirPath)
            if (!dir.exists()) {
                dir.mkdir()
            }
            return dir
        }

    fun createFile(fileName: String?): File {
        val name = fileName ?: "${UUID.randomUUID()}_${System.currentTimeMillis()}"
        return File(fileDir, name)
    }
}