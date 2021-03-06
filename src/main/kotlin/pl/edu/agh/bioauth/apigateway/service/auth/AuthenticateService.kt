package pl.edu.agh.bioauth.apigateway.service.auth

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.auth.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.model.network.service.request.RecognitionRequest
import pl.edu.agh.bioauth.apigateway.model.network.service.response.RecognitionResponse
import pl.edu.agh.bioauth.apigateway.service.common.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.common.ErrorService
import pl.edu.agh.bioauth.apigateway.service.common.FileService
import pl.edu.agh.bioauth.apigateway.service.common.HttpService
import pl.edu.agh.bioauth.apigateway.service.common.SecurityService
import pl.edu.agh.bioauth.apigateway.util.extension.deleteAll
import pl.edu.agh.bioauth.apigateway.util.extension.getPaths
import pl.edu.agh.bioauth.apigateway.util.extension.path
import pl.edu.agh.bioauth.apigateway.util.extension.toPrivateKey
import javax.servlet.http.HttpServletRequest

abstract class AuthenticateService {

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var httpService: HttpService

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var securityService: SecurityService

    @Autowired
    private lateinit var errorService: ErrorService

    @Autowired
    private lateinit var request: HttpServletRequest

    @Throws(RequestException::class)
    abstract fun authenticate(
            samples: List<MultipartFile>,
            appId: String,
            appSecret: String,
            challenge: String,
            userId: String?,
            keyId: String
    ): AuthenticateResponse

    protected fun recognizeSamples(samples: List<MultipartFile>,
                                   appId: String,
                                   appSecret: String,
                                   challenge: String,
                                   userId: String?,
                                   keyId: String,
                                   patternType: BiometricPattern.Type): AuthenticateResponse {

        val app = databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound(request.path)
        val key = databaseService.findEncryptionKey(ObjectId(keyId)) ?: errorService.failWithInternalError(request.path)

        val biometricPatterns = databaseService.findPatternsByAppAndUser(app._id, userId)

        val livenessStatus = fileService.getLivenessStatus(samples, key)
        val files = fileService.saveSamples(samples, key)

        val patterns = biometricPatterns.map { it.userId to it.filePaths }.toMap()

        val doFinally: () -> Unit = {
            files.deleteAll()
            databaseService.deleteEncryptionKey(key)
        }

        errorService.cleanUp = doFinally

        try {
            val response = recognize(RecognitionRequest(files.getPaths(), livenessStatus, patterns), patternType)

            with(response) {
                if (statusCode == HttpStatus.OK) {
                    val matchedUserId = body?.userId ?: errorService.failWithAuthenticationError(request.path)
                    val pattern = biometricPatterns.find { it.userId == matchedUserId }
                            ?: errorService.failWithInternalError(request.path)
                    val signedChallenge = securityService.signString(challenge, pattern.privateKey.toPrivateKey())

                    doFinally()
                    return AuthenticateResponse(matchedUserId, signedChallenge)
                } else {
                    errorService.failWithServiceError(statusCode, request.path)
                }
            }
        } catch (e: HttpStatusCodeException) {
            val response = if (e.responseBodyAsString.isNotBlank()) e.responseBodyAsString else null
            errorService.failWithServiceError(e.statusCode, request.path, response)
        }
    }

    @Throws(RequestException::class)
    private fun recognize(recognitionRequest: RecognitionRequest,
                          patternType: BiometricPattern.Type): ResponseEntity<RecognitionResponse> =
            with(httpService) {
                val path = getBiometricRecognitionPath(patternType)
                        ?: errorService.failWithServiceError(HttpStatus.BAD_REQUEST, request.path)
                return post(path, recognitionRequest, RecognitionResponse::class)
            }
}