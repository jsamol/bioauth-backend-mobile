package pl.edu.agh.bioauth.apigateway.service.common

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.model.database.App
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.database.EncryptionKey
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository
import pl.edu.agh.bioauth.apigateway.repository.EncryptionKeyRepository

@Service
class DatabaseService(private val appRepository: AppRepository,
                      private val biometricPatternRepository: BiometricPatternRepository,
                      private val encryptionKeyRepository: EncryptionKeyRepository) {

    fun getApp(appId: String, appSecret: String): App? =
            appRepository.findByAppIdAndAppSecret(appId, appSecret)

    fun findPatternsByApp(appId: ObjectId): List<BiometricPattern> = biometricPatternRepository.findByAppId(appId)

    fun savePattern(biometricPattern: BiometricPattern): BiometricPattern = biometricPatternRepository.save(biometricPattern)

    fun saveEnryptionKey(encryptionKey: EncryptionKey): EncryptionKey = encryptionKeyRepository.save(encryptionKey)
}