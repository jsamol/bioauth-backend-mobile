package pl.edu.agh.bioauth.apigateway.util

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.*

fun MultipartFile.toFile(): File = File(System.getProperty("java.io.tmpdir"), originalFilename).also { transferTo(it) }

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