package pl.edu.agh.bioauth.apigateway.service.common

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.model.database.EncryptionKey
import pl.edu.agh.bioauth.apigateway.util.extension.decode64
import pl.edu.agh.bioauth.apigateway.util.extension.getMetadata
import pl.edu.agh.bioauth.apigateway.util.extension.getSamples
import pl.edu.agh.bioauth.apigateway.util.extension.toSymmetricKey
import java.io.File
import java.util.*

@Service
class FileService(private val securityService: SecurityService,
                  private val metadataService: MetadataService) {

    val patternDirPath: String =
            "${System.getProperty("user.home")}${File.separator}bioauth${File.separator}data${File.separator}patterns"

    private val tempDir: String = System.getProperty("java.io.tmpdir")

    private val randomFileName: String
        get() = "${UUID.randomUUID()}_${System.currentTimeMillis()}"


    fun getLivenessStatus(samples: List<MultipartFile>, encryptionKey: EncryptionKey): Boolean {
        val metadata = samples.getMetadata()?.bytes?.let { decryptData(it, encryptionKey) }

        return (metadata != null && metadataService.wasLivenessTested(metadata))
    }

    fun saveSamples(samples: List<MultipartFile>, encryptionKey: EncryptionKey): List<File> =
            samples.getSamples().map {
                val bytes = decryptData(it.bytes, encryptionKey)
                createTempFile(it.originalFilename).apply { writeBytes(bytes) }
            }

    private fun decryptData(data: ByteArray, encryptionKey: EncryptionKey): ByteArray =
            with (encryptionKey) {
                securityService.decryptData(data, value.toSymmetricKey(), iv.decode64())
            }

    private fun createTempFile(fileName: String?): File = File(tempDir, fileName ?: randomFileName)

    object FileType {
        const val JSON = ".json"
    }
}