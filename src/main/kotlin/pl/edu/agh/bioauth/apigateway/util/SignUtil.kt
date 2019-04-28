package pl.edu.agh.bioauth.apigateway.util

import java.security.PrivateKey
import java.security.Signature

object SignUtil {

    private const val SIGNATURE_ALGORITHM = "SHA256WithRSA"

    private val signature: Signature by lazy { Signature.getInstance(SIGNATURE_ALGORITHM) }

    fun signString(toSign: String, key: PrivateKey): ByteArray =
            with(signature) {
                initSign(key)
                update(toSign.toByteArray())
                sign()
            }

}