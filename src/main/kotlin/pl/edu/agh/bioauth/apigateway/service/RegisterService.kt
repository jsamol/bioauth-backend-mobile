package pl.edu.agh.bioauth.apigateway.service

import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.RegisterResponse
import pl.edu.agh.bioauth.apigateway.util.KeyGenerator
import pl.edu.agh.bioauth.apigateway.util.extension.stringValue
import pl.edu.agh.bioauth.apigateway.util.extension.save
import java.security.KeyPair

abstract class RegisterService : BioAuthService() {

    private val keyPair: KeyPair
        get() = KeyGenerator.getKeyPair()

    @Throws(AppNotFoundException::class)
    abstract fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String): RegisterResponse

    protected fun saveBiometricPattern(samples: List<MultipartFile>,
                                       appId: String, appSecret: String,
                                       userId: String,
                                       type: BiometricPattern.Type) : RegisterResponse {

        val app = getApp(appId, appSecret) ?: failWithAppNotFound()
        val filePaths = saveSamples(samples)
        biometricPatternRepository.save(BiometricPattern(filePaths, app._id, userId, keyPair.private.stringValue, type))

        return RegisterResponse(keyPair.public.stringValue)
    }
}