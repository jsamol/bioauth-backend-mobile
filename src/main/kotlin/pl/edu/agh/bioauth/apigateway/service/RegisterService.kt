package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.RegisterResponse
import pl.edu.agh.bioauth.apigateway.service.helper.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.helper.ErrorService
import pl.edu.agh.bioauth.apigateway.service.helper.FileService
import pl.edu.agh.bioauth.apigateway.util.KeyGenerator
import pl.edu.agh.bioauth.apigateway.util.extension.stringValue
import java.security.KeyPair

abstract class RegisterService {

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var errorService: ErrorService

    private val keyPair: KeyPair
        get() = KeyGenerator.getKeyPair()

    @Throws(AppNotFoundException::class)
    abstract fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String): RegisterResponse

    protected fun saveBiometricPattern(samples: List<MultipartFile>,
                                       appId: String, appSecret: String,
                                       userId: String,
                                       type: BiometricPattern.Type) : RegisterResponse {

        val app = databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound()
        val filePaths = fileService.saveSamples(samples)
        databaseService.savePattern(BiometricPattern(filePaths, app._id, userId, keyPair.private.stringValue, type))

        return RegisterResponse(keyPair.public.stringValue)
    }
}