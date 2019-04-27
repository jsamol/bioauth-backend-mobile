package pl.edu.agh.bioauth.apigateway.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.ApplicationProperties
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.RecognitionFailedException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.RegisterResponse
import pl.edu.agh.bioauth.apigateway.model.network.service.RecognitionResponse
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.SAMPLE
import pl.edu.agh.bioauth.apigateway.util.KeyGenerator
import pl.edu.agh.bioauth.apigateway.util.SignUtil
import pl.edu.agh.bioauth.apigateway.util.addAll
import pl.edu.agh.bioauth.apigateway.util.stringValue
import pl.edu.agh.bioauth.apigateway.util.toFile
import pl.edu.agh.bioauth.apigateway.util.toPrivateKey
import java.io.File

@Service
class FaceRecognitionService(private val appRepository: AppRepository,
                             private val biometricPatternRepository: BiometricPatternRepository,
                             private val gridFsTemplate: GridFsTemplate,
                             private val applicationProperties: ApplicationProperties,
                             restTemplateBuilder: RestTemplateBuilder) {

    private val restTemplate: RestTemplate by lazy { restTemplateBuilder.build() }

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

    @Throws(AppNotFoundException::class, RecognitionFailedException::class, AuthenticationFailedException::class)
    fun authenticate(samples: List<MultipartFile>, appId: String, appSecret: String, challenge: String): AuthenticateResponse {
        appRepository.findByAppIdAndAppSecret(appId, appSecret)?.let { app ->
            val response = recognize(samples[0].toFile(), app.id)
            with (response) {
                if (statusCode == HttpStatus.OK) {
                    body?.userId?.let{ userId ->
                        val pattern = biometricPatternRepository.findByAppIdAndUserId(app._id, userId)
                        val signedChallenge = SignUtil.signString(challenge, pattern.privateKey.toPrivateKey())
                        return AuthenticateResponse(userId, signedChallenge)
                    } ?: throw AuthenticationFailedException()
                } else {
                    throw RecognitionFailedException(statusCodeValue)
                }
            }
        }

        throw AppNotFoundException()
    }

    private fun recognize(sample: File, appId: String): ResponseEntity<RecognitionResponse> {
        val requestParams = mapOf(
                SAMPLE to sample,
                APP_ID to appId
        )
        val requestEntity = getMultipartRequest(requestParams)
        return restTemplate.postForEntity(applicationProperties.faceRecognitionPath, requestEntity)
    }

    private fun getMultipartRequest(params: Map<String, Any>): HttpEntity<MultiValueMap<String, Any>> {
        val httpHeaders = HttpHeaders().apply { contentType = MediaType.MULTIPART_FORM_DATA }
        val requestBody = LinkedMultiValueMap<String, Any>().apply { addAll(params) }

        return HttpEntity(requestBody, httpHeaders)
    }
}