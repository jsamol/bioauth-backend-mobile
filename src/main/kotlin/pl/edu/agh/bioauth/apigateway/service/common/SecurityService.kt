package pl.edu.agh.bioauth.apigateway.service.common

import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.util.extension.encode64
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.Signature
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@Service
class SecurityService {

    val iv: ByteArray
        get() = IvParameterSpec(ByteArray(IV_SIZE).also { secureRandom.nextBytes(it) }).iv

    private val secureRandom: SecureRandom
        get() = SecureRandom()

    private val signature: Signature by lazy { Signature.getInstance(SIGNATURE_ALGORITHM) }

    fun getKeyPair(): KeyPair =
            KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM)
                    .apply { initialize(KEY_PAIR_SIZE, secureRandom) }
                    .genKeyPair()

    fun getSymmetricKey(): SecretKey =
            KeyGenerator.getInstance(KEY_SYMMETRIC_ALGORITHM)
                    .apply { init(KEY_SYMMETRIC_SIZE, secureRandom) }
                    .generateKey()

    fun signString(toSign: String, key: PrivateKey): String =
            with(signature) {
                initSign(key)
                update(toSign.toByteArray())
                sign().encode64()
            }

    fun encryptData(data: ByteArray, key: Key): String =
            Cipher.getInstance(ENCRYPTION_TRANSFORMATION)
                    .apply { init(Cipher.ENCRYPT_MODE, key) }
                    .doFinal(data)
                    .let { it.encode64() }

    fun decryptData(data: ByteArray, key: Key, iv: ByteArray): ByteArray =
            Cipher.getInstance(DECRYPTION_TRANSFORMATION)
                .apply { init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv)) }
                .doFinal(data)


    companion object {
        const val KEY_PAIR_ALGORITHM = "RSA"
        const val KEY_SYMMETRIC_ALGORITHM = "AES"

        private const val KEY_PAIR_SIZE = 1024
        private const val KEY_SYMMETRIC_SIZE = 256
        private const val IV_SIZE = 16

        private const val SIGNATURE_ALGORITHM = "SHA256WithRSA"

        private const val ENCRYPTION_TRANSFORMATION = "RSA/ECB/OAEPWithSHA1AndMGF1Padding"
        private const val DECRYPTION_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    }
}