package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.RegisterResponse
import pl.edu.agh.bioauth.apigateway.service.helper.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.helper.ErrorService
import pl.edu.agh.bioauth.apigateway.service.helper.SecurityService
import pl.edu.agh.bioauth.apigateway.util.extension.getPaths
import pl.edu.agh.bioauth.apigateway.util.extension.path
import pl.edu.agh.bioauth.apigateway.util.extension.saveAll
import pl.edu.agh.bioauth.apigateway.util.extension.stringValue
import java.security.KeyPair
import javax.servlet.http.HttpServletRequest

abstract class RegisterService {

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var securityService: SecurityService

    @Autowired
    private lateinit var errorService: ErrorService

    @Autowired
    private lateinit var request: HttpServletRequest

    private val keyPair: KeyPair by lazy { securityService.getKeyPair() }

    @Throws(RequestException::class)
    abstract fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String): RegisterResponse

    protected fun saveBiometricPattern(samples: List<MultipartFile>,
                                       appId: String, appSecret: String,
                                       userId: String,
                                       type: BiometricPattern.Type) : RegisterResponse {

        val app = databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound(request.path)
        val filePaths = samples.saveAll().getPaths()
        databaseService.savePattern(BiometricPattern(filePaths, app._id, userId, keyPair.private.stringValue, type))

        return RegisterResponse(keyPair.public.stringValue)
    }
}