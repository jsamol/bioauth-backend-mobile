package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.RegisterResponse
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository
import pl.edu.agh.bioauth.apigateway.util.KeyGenerator
import pl.edu.agh.bioauth.apigateway.util.stringValue
import pl.edu.agh.bioauth.apigateway.util.toFile

@Service
class AuthService {

    @Autowired
    private lateinit var appRepository: AppRepository

    @Autowired
    private lateinit var biometricPatternRepository: BiometricPatternRepository

    @Autowired
    private lateinit var gridFsTemplate: GridFsTemplate

    @Throws(AppNotFoundException::class)
    fun registerPattern(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String): RegisterResponse {
        appRepository.findByAppIdAndAppSecret(appId, appSecret)?.let { app ->
            val fileIds = samples.map(MultipartFile::toFile).map { gridFsTemplate.store(it.inputStream(), it.name) }
            val keyPair = KeyGenerator.getKeyPair()
            biometricPatternRepository.save(BiometricPattern(fileIds, app._id, userId, keyPair.private.stringValue))

            return RegisterResponse(keyPair.public.stringValue)
        }

        throw AppNotFoundException()
    }
}