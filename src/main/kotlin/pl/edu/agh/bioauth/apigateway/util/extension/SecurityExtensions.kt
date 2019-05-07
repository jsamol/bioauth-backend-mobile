package pl.edu.agh.bioauth.apigateway.util.extension

import pl.edu.agh.bioauth.apigateway.service.common.SecurityService
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

val Key.stringValue: String
    get() {
        val keyFactory = KeyFactory.getInstance(SecurityService.KEY_ALGORITHM)
        val keySpecType = when (this) {
            is PrivateKey -> PKCS8EncodedKeySpec::class.java
            is PublicKey -> X509EncodedKeySpec::class.java
            else -> null
        } ?: return toString()

        val keySpec = keyFactory.getKeySpec(this, keySpecType)

        return Base64.getEncoder().encodeToString(keySpec.encoded)
    }

fun String.toPrivateKey(): PrivateKey {
    val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(this))
    val keyFactory = KeyFactory.getInstance(SecurityService.KEY_ALGORITHM)

    return keyFactory.generatePrivate(keySpec)
}