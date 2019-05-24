package pl.edu.agh.bioauth.apigateway.service.auth

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.auth.RegisterResponse
import pl.edu.agh.bioauth.apigateway.model.network.service.request.PatternsRequest
import pl.edu.agh.bioauth.apigateway.model.network.service.response.PatternsResponse
import pl.edu.agh.bioauth.apigateway.service.common.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.common.ErrorService
import pl.edu.agh.bioauth.apigateway.service.common.FileService
import pl.edu.agh.bioauth.apigateway.service.common.HttpService
import pl.edu.agh.bioauth.apigateway.service.common.SecurityService
import pl.edu.agh.bioauth.apigateway.util.extension.deleteAll
import pl.edu.agh.bioauth.apigateway.util.extension.getPaths
import pl.edu.agh.bioauth.apigateway.util.extension.path
import pl.edu.agh.bioauth.apigateway.util.extension.stringValue
import java.security.KeyPair
import javax.servlet.http.HttpServletRequest

abstract class RegisterService {

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

    private val keyPair: KeyPair by lazy { securityService.getKeyPair() }

    @Throws(RequestException::class)
    abstract fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String, keyId: String): RegisterResponse

    protected fun saveBiometricPattern(samples: List<MultipartFile>,
                                       appId: String, appSecret: String,
                                       userId: String,
                                       keyId: String,
                                       patternType: BiometricPattern.Type) : RegisterResponse {

        val app = databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound(request.path)
        val key = databaseService.findEncryptionKey(ObjectId(keyId)) ?: errorService.failWithInternalError(request.path)

        val livenessStatus = fileService.getLivenessStatus(samples, key)
        val files = fileService.saveSamples(samples, key)

        val doFinally: () -> Unit = {
            files.deleteAll()
            databaseService.deleteEncryptionKey(key)
        }

        errorService.cleanUp = doFinally

        try {
            val response =
                    extractBiometricPatterns(PatternsRequest(files.getPaths(), livenessStatus, fileService.patternDirPath), patternType)

            with(response) {
                if (statusCode == HttpStatus.OK) {
                    val filePaths = body?.filePaths

                    if (filePaths == null || filePaths.isEmpty()) {
                        errorService.failWithRegistrationError(request.path)
                    }

                    databaseService.savePattern(BiometricPattern(filePaths, app._id, userId, keyPair.private.stringValue, patternType))

                    doFinally()
                    return RegisterResponse(keyPair.public.stringValue)
                } else {
                    errorService.failWithServiceError(statusCode, request.path)
                }
            }
        } catch (e: HttpStatusCodeException) {
            errorService.failWithServiceError(e.statusCode, request.path)
        }
    }

    private fun extractBiometricPatterns(patternsRequest: PatternsRequest,
                                         patternType: BiometricPattern.Type): ResponseEntity<PatternsResponse> =
            with (httpService) {
                val path = getBiometricPatternExtractionPath(patternType)
                        ?: errorService.failWithServiceError(HttpStatus.BAD_REQUEST, request.path)
                return post(path, patternsRequest, PatternsResponse::class)
            }
}