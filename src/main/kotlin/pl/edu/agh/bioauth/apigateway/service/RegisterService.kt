package pl.edu.agh.bioauth.apigateway.service

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.RegisterResponse
import pl.edu.agh.bioauth.apigateway.util.KeyGenerator
import pl.edu.agh.bioauth.apigateway.util.stringValue
import pl.edu.agh.bioauth.apigateway.util.toFile
import java.security.KeyPair

abstract class RegisterService : BioAuthService() {

    @Autowired
    private lateinit var gridFsTemplate: GridFsTemplate

    private val keyPair: KeyPair
        get() = KeyGenerator.getKeyPair()

    @Throws(AppNotFoundException::class)
    abstract fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String): RegisterResponse

    protected fun saveBiometricPattern(samples: List<MultipartFile>,
                                       appId: String, appSecret: String,
                                       userId: String,
                                       type: BiometricPattern.Type) : RegisterResponse {

        val app = getApp(appId, appSecret) ?: failWithAppNotFound()
        val fileIds = saveSamples(samples)
        biometricPatternRepository.save(BiometricPattern(fileIds, app._id, userId, keyPair.private.stringValue, type))

        return RegisterResponse(keyPair.public.stringValue)
    }

    private fun saveSamples(samples: List<MultipartFile>): List<ObjectId> =
            samples.map(MultipartFile::toFile).map { gridFsTemplate.store(it.inputStream(), it.name) }
}