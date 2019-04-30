package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.ApplicationProperties
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.ServiceFailureException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.model.network.service.request.RecognitionRequest
import pl.edu.agh.bioauth.apigateway.model.network.service.response.RecognitionResponse
import pl.edu.agh.bioauth.apigateway.util.SignUtil
import pl.edu.agh.bioauth.apigateway.util.extension.toPrivateKey

abstract class AuthenticateService : BioAuthService() {

    @Autowired
    protected lateinit var applicationProperties: ApplicationProperties

    @Autowired
    private lateinit var restTemplateBuilder: RestTemplateBuilder

    private val restTemplate: RestTemplate by lazy { restTemplateBuilder.build() }

    @Throws(AppNotFoundException::class, ServiceFailureException::class, AuthenticationFailedException::class)
    abstract fun authenticate(samples: List<MultipartFile>, appId: String, appSecret: String, challenge: String): AuthenticateResponse

    protected fun recognizeSamples(samples: List<MultipartFile>,
                                   appId: String,
                                   appSecret: String,
                                   challenge: String,
                                   type: BiometricPattern.Type): AuthenticateResponse {

        val app = getApp(appId, appSecret) ?: failWithAppNotFound()
        val biometricPatterns = biometricPatternRepository.findByAppId(app._id)

        val samplePaths = saveSamples(samples, temp = true)
        val patterns = biometricPatterns.map { it.userId to it.filePaths }.toMap()

        val response = recognize(RecognitionRequest(samplePaths, patterns), type)

        with (response) {
            if (statusCode == HttpStatus.OK) {
                val userId = body?.userId ?: failWithAuthenticationError()
                val pattern = biometricPatternRepository.findByAppIdAndUserId(app._id, userId)
                val signedChallenge = SignUtil.signString(challenge, pattern.privateKey.toPrivateKey())

                return AuthenticateResponse(userId, signedChallenge)
            } else {
                failWithServiceError(statusCodeValue)
            }
        }

    }

    @Throws(ServiceFailureException::class)
    private fun recognize(recognitionRequest: RecognitionRequest, type: BiometricPattern.Type): ResponseEntity<RecognitionResponse> {
        val requestEntity = getHttpRequest(recognitionRequest)
        val path = applicationProperties.biometricMethodsPaths[type] ?: failWithServiceError(HttpStatus.BAD_REQUEST.value())

        return restTemplate.postForEntity(path, requestEntity)
    }

    private fun getHttpRequest(recognitionRequest: RecognitionRequest): HttpEntity<RecognitionRequest> {
        val httpHeaders = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        return HttpEntity(recognitionRequest, httpHeaders)
    }

    private fun failWithServiceError(status: Int): Nothing = throw ServiceFailureException(status)

    private fun failWithAuthenticationError(): Nothing = throw AuthenticationFailedException()
}