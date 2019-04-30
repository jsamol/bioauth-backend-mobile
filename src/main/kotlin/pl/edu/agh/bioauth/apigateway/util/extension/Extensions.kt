package pl.edu.agh.bioauth.apigateway.util.extension

import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.HandlerMapping
import pl.edu.agh.bioauth.apigateway.util.FileManager
import pl.edu.agh.bioauth.apigateway.util.KeyGenerator
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.servlet.http.HttpServletRequest

fun MultipartFile.save(): String = FileManager.createFile(originalFilename).also { transferTo(it) }.absolutePath

fun MultipartFile.saveTemp(): String = FileManager.createTempFile(originalFilename).also { transferTo(it) }.absolutePath

val PrivateKey.stringValue: String
    get() {
        val keyFactory = KeyFactory.getInstance(KeyGenerator.KEY_ALGORITHM)
        val keySpec = keyFactory.getKeySpec(this, PKCS8EncodedKeySpec::class.java)

        return Base64.getEncoder().encodeToString(keySpec.encoded)
    }

val PublicKey.stringValue: String
    get() {
        val keyFactory = KeyFactory.getInstance(KeyGenerator.KEY_ALGORITHM)
        val keySpec = keyFactory.getKeySpec(this, X509EncodedKeySpec::class.java)

        return Base64.getEncoder().encodeToString(keySpec.encoded)
    }

fun String.toPrivateKey(): PrivateKey {
    val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(this))
    val keyFactory = KeyFactory.getInstance(KeyGenerator.KEY_ALGORITHM)
    return keyFactory.generatePrivate(keySpec)
}

val HttpServletRequest.path: String
    get() = getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()