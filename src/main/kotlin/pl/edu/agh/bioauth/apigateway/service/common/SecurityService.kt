package pl.edu.agh.bioauth.apigateway.service.common

import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Service
class SecurityService {

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

    fun signString(toSign: String, key: PrivateKey): ByteArray =
            with(signature) {
                initSign(key)
                update(toSign.toByteArray())
                sign()
            }

    fun encryptString(toEncrypt: String, key: PublicKey): ByteArray =
            Cipher.getInstance(CIPHER_TYPE)
                    .apply { init(Cipher.ENCRYPT_MODE, key) }
                    .doFinal(toEncrypt.toByteArray())


    companion object {
        private const val KEY_PAIR_SIZE = 1024
        private const val KEY_SYMMETRIC_SIZE = 256

        private const val SIGNATURE_ALGORITHM = "SHA256WithRSA"
        private const val CIPHER_TYPE = "RSA/ECB/OAEPWithSHA1AndMGF1Padding"

        const val KEY_PAIR_ALGORITHM: String = "RSA"
        const val KEY_SYMMETRIC_ALGORITHM: String = "AES"
    }
}