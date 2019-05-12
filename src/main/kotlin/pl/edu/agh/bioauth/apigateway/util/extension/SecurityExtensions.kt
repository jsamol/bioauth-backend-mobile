package pl.edu.agh.bioauth.apigateway.util.extension

import pl.edu.agh.bioauth.apigateway.service.common.SecurityService
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.SecretKey

val Key.stringValue: String
    get() {
        fun getEncodedAsymmetricKey(): ByteArray {
            val keyFactory = KeyFactory.getInstance(SecurityService.KEY_PAIR_ALGORITHM)
            val keySpecType = when (this) {
                is PrivateKey -> PKCS8EncodedKeySpec::class.java
                is PublicKey -> X509EncodedKeySpec::class.java
                else -> null
            } ?: return encoded

            return keyFactory.getKeySpec(this, keySpecType).encoded
        }

        fun getEncodedSymmetricKey(): ByteArray = encoded

        val encodedKey = when (this) {
            is PrivateKey, is PublicKey -> getEncodedAsymmetricKey()
            is SecretKey -> getEncodedSymmetricKey()
            else -> null
        } ?: return toString()

        return Base64.getEncoder().encodeToString(encodedKey)
    }

fun String.toPrivateKey(): PrivateKey {
    val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(this))
    val keyFactory = KeyFactory.getInstance(SecurityService.KEY_PAIR_ALGORITHM)

    return keyFactory.generatePrivate(keySpec)
}

fun String.toPublicKey(): PublicKey {
    val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(this))
    val keyFactory = KeyFactory.getInstance(SecurityService.KEY_PAIR_ALGORITHM)

    return keyFactory.generatePublic(keySpec)
}