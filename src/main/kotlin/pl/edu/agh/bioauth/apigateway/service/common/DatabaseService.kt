package pl.edu.agh.bioauth.apigateway.service.common

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.model.database.App
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository

@Service
class DatabaseService(private val appRepository: AppRepository,
                      private val biometricPatternRepository: BiometricPatternRepository) {

    fun getApp(appId: String, appSecret: String): App? =
            appRepository.findByAppIdAndAppSecret(appId, appSecret)

    fun findPatternsByApp(appId: ObjectId): List<BiometricPattern> = biometricPatternRepository.findByAppId(appId)

    fun savePattern(biometricPattern: BiometricPattern) = biometricPatternRepository.save(biometricPattern)
}