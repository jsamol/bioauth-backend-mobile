package pl.edu.agh.bioauth.apigateway.util

import org.springframework.http.ContentDisposition
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
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

fun File.toMultipartEntity(): HttpEntity<File> {
    val contentDisposition = ContentDisposition
            .builder("form-data")
            .name("file")
            .filename(name)
            .build()
    val fileMap = LinkedMultiValueMap<String, String>().apply {
        add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
    }
    return HttpEntity(this, fileMap)
}

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

fun <K, V> MultiValueMap<K, V>.addAll(map: Map<K, V>) {
    map.forEach { add(it.key, it.value) }
}