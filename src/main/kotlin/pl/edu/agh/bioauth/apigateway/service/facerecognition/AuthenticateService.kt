package pl.edu.agh.bioauth.apigateway.service.facerecognition

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
import pl.edu.agh.bioauth.apigateway.model.network.api.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.model.network.service.RecognitionResponse
import pl.edu.agh.bioauth.apigateway.repository.AppRepository
import pl.edu.agh.bioauth.apigateway.repository.BiometricPatternRepository
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam
import pl.edu.agh.bioauth.apigateway.util.SignUtil
import pl.edu.agh.bioauth.apigateway.util.addAll
import pl.edu.agh.bioauth.apigateway.util.toFile
import pl.edu.agh.bioauth.apigateway.util.toMultipartEntity
import pl.edu.agh.bioauth.apigateway.util.toPrivateKey
import java.io.File

@Service
class AuthenticateService(private val appRepository: AppRepository,
                          private val biometricPatternRepository: BiometricPatternRepository,
                          private val applicationProperties: ApplicationProperties,
                          restTemplateBuilder: RestTemplateBuilder) {

    private val restTemplate: RestTemplate by lazy { restTemplateBuilder.build() }

    @Throws(AppNotFoundException::class, RecognitionFailedException::class, AuthenticationFailedException::class)
    fun authenticate(samples: List<MultipartFile>, appId: String, appSecret: String, challenge: String): AuthenticateResponse {
        appRepository.findByAppIdAndAppSecret(appId, appSecret)?.let { app ->
            val response = recognize(samples.map(MultipartFile::toFile), app.id)
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

    private fun recognize(samples: List<File>, appId: String): ResponseEntity<RecognitionResponse> {
        val files = mapOf(AuthRequestParam.SAMPLES to samples)
        val data = mapOf(AuthRequestParam.APP_ID to appId)
        val requestEntity = getMultipartRequest(files, data)
        return restTemplate.postForEntity(applicationProperties.faceRecognitionPath, requestEntity)
    }

    private fun getMultipartRequest(files: Map<String, List<File>>, data: Map<String, Any>): HttpEntity<MultiValueMap<String, Any>> {
        val httpHeaders = HttpHeaders().apply { contentType = MediaType.MULTIPART_FORM_DATA }
        val fileEntities = files.mapValues { it.value.map { file -> file.toMultipartEntity(it.key) } }

        val requestBody = LinkedMultiValueMap<String, Any>().apply {
            fileEntities.forEach { (key, files) ->
                files.forEach { add(key, it) }
            }
            addAll(data)
        }

        return HttpEntity(requestBody, httpHeaders)
    }
}