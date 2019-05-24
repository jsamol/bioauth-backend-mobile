package pl.edu.agh.bioauth.apigateway.service.common

import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.model.database.App
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.database.EncryptionKey
import pl.edu.agh.bioauth.apigateway.model.database.Statistics
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository
import pl.edu.agh.bioauth.apigateway.repository.EncryptionKeyRepository
import pl.edu.agh.bioauth.apigateway.repository.StatisticsRepository

@Service
class DatabaseService(private val appRepository: AppRepository,
                      private val biometricPatternRepository: BiometricPatternRepository,
                      private val encryptionKeyRepository: EncryptionKeyRepository,
                      private val statisticsRepository: StatisticsRepository) {

    fun getApp(appId: String, appSecret: String): App? =
            appRepository.findByAppIdAndAppSecret(appId, appSecret)

    fun findPatternsByAppAndUser(appId: ObjectId, userId: String?): List<BiometricPattern> =
            userId?.let {
                biometricPatternRepository.findByAppIdAndUserId(appId, it)
            } ?: biometricPatternRepository.findByAppId(appId)

    fun savePattern(biometricPattern: BiometricPattern): BiometricPattern = biometricPatternRepository.save(biometricPattern)

    fun saveEncryptionKey(encryptionKey: EncryptionKey): EncryptionKey = encryptionKeyRepository.save(encryptionKey)

    fun findEncryptionKey(keyId: ObjectId): EncryptionKey? = encryptionKeyRepository.findByIdOrNull(keyId)

    fun deleteEncryptionKey(encryptionKey: EncryptionKey) = encryptionKeyRepository.delete(encryptionKey)

    fun saveStatistics(statistics: Statistics): Statistics = statisticsRepository.save(statistics)
}