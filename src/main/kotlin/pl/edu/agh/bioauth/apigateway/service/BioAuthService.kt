package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.App
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository
import pl.edu.agh.bioauth.apigateway.util.extension.save
import pl.edu.agh.bioauth.apigateway.util.extension.saveTemp

abstract class BioAuthService {

    @Autowired
    protected lateinit var appRepository: AppRepository

    @Autowired
    protected lateinit var biometricPatternRepository: BiometricPatternRepository

    protected fun getApp(appId: String, appSecret: String): App? =
            appRepository.findByAppIdAndAppSecret(appId, appSecret)

    protected fun saveSamples(samples: List<MultipartFile>, temp: Boolean = false): List<String> =
            samples.map(if (temp) MultipartFile::saveTemp else MultipartFile::save)

    protected fun failWithAppNotFound(): Nothing = throw AppNotFoundException()
}