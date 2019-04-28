package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.App
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository

abstract class BioAuthService {

    @Autowired
    protected lateinit var appRepository: AppRepository

    @Autowired
    protected lateinit var biometricPatternRepository: BiometricPatternRepository

    protected fun getApp(appId: String, appSecret: String): App? =
            appRepository.findByAppIdAndAppSecret(appId, appSecret)

    protected fun failWithAppNotFound(): Nothing = throw AppNotFoundException()
}