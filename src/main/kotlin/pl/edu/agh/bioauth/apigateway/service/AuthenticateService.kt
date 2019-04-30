package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.InternalFailureException
import pl.edu.agh.bioauth.apigateway.exception.ServiceFailureException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.model.network.service.request.RecognitionRequest
import pl.edu.agh.bioauth.apigateway.model.network.service.response.RecognitionResponse
import pl.edu.agh.bioauth.apigateway.service.helper.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.helper.ErrorService
import pl.edu.agh.bioauth.apigateway.service.helper.FileService
import pl.edu.agh.bioauth.apigateway.service.helper.HttpService
import pl.edu.agh.bioauth.apigateway.util.SignUtil
import pl.edu.agh.bioauth.apigateway.util.extension.toPrivateKey

abstract class AuthenticateService {

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var httpService: HttpService

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var errorService: ErrorService

    @Throws(AppNotFoundException::class,
            ServiceFailureException::class,
            AuthenticationFailedException::class,
            InternalFailureException::class)
    abstract fun authenticate(samples: List<MultipartFile>, appId: String, appSecret: String, challenge: String): AuthenticateResponse

    protected fun recognizeSamples(samples: List<MultipartFile>,
                                   appId: String,
                                   appSecret: String,
                                   challenge: String,
                                   patternType: BiometricPattern.Type): AuthenticateResponse {

        val app = databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound()
        val biometricPatterns = databaseService.findPatternsByApp(app._id)

        val samplePaths = fileService.saveSamples(samples, temp = true)
        val patterns = biometricPatterns.map { it.userId to it.filePaths }.toMap()

        val response = recognize(RecognitionRequest(samplePaths, patterns), patternType)

        with(response) {
            if (statusCode == HttpStatus.OK) {
                val userId = body?.userId ?: errorService.failWithAuthenticationError()
                val pattern = biometricPatterns.find { it.userId == userId } ?: errorService.failWithInternalError()
                val signedChallenge = SignUtil.signString(challenge, pattern.privateKey.toPrivateKey())

                return AuthenticateResponse(userId, signedChallenge)
            } else {
                errorService.failWithServiceError(statusCode)
            }
        }

    }

    @Throws(ServiceFailureException::class)
    private fun recognize(recognitionRequest: RecognitionRequest,
                          patternType: BiometricPattern.Type): ResponseEntity<RecognitionResponse> =
            with(httpService) {
                val path = getBiometricServicePath(patternType) ?: errorService.failWithServiceError(HttpStatus.BAD_REQUEST)
                return post(path, recognitionRequest, RecognitionResponse::class)
            }
}