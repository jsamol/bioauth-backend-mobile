package pl.edu.agh.bioauth.apigateway.util

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

object KeyGenerator {

    private const val ECDH = "DiffieHellman"
    private const val RSA = "RSA"
    private const val DSA = "DSA"

    private const val KEY_SIZE = 1024

    const val KEY_ALGORITHM: String = ECDH

    private val secureRandom: SecureRandom
        get() = SecureRandom()

    fun getKeyPair(): KeyPair =
            KeyPairGenerator.getInstance(KEY_ALGORITHM)
                    .apply { initialize(KEY_SIZE, secureRandom) }
                    .genKeyPair()
}