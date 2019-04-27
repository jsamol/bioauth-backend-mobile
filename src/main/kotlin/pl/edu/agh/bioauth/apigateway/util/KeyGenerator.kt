package pl.edu.agh.bioauth.apigateway.util

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

object KeyGenerator {
    private const val KEY_SIZE = 2048

    const val KEY_ALGORITHM: String = "RSA"

    private val secureRandom: SecureRandom
        get() = SecureRandom()

    fun getKeyPair(): KeyPair =
            KeyPairGenerator.getInstance(KEY_ALGORITHM)
                    .apply { initialize(KEY_SIZE, secureRandom) }
                    .genKeyPair()
}