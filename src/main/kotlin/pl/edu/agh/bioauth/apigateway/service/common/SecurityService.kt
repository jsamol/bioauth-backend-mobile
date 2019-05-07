package pl.edu.agh.bioauth.apigateway.service.common

import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.Signature

@Service
class SecurityService {

    private val secureRandom: SecureRandom
        get() = SecureRandom()

    private val signature: Signature by lazy { Signature.getInstance(SIGNATURE_ALGORITHM) }

    fun getKeyPair(): KeyPair =
            KeyPairGenerator.getInstance(KEY_ALGORITHM)
                    .apply { initialize(KEY_SIZE, secureRandom) }
                    .genKeyPair()

    fun signString(toSign: String, key: PrivateKey): String =
            with(signature) {
                initSign(key)
                update(toSign.toByteArray())
                sign()
            }.toString()

    companion object {
        private const val KEY_SIZE = 1024
        private const val SIGNATURE_ALGORITHM = "SHA256WithRSA"

        const val KEY_ALGORITHM: String = "RSA"
    }
}