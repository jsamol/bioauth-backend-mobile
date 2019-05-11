package pl.edu.agh.bioauth.apigateway.service.encryption

import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.database.EncryptionKey
import pl.edu.agh.bioauth.apigateway.model.network.api.encryption.SymmetricKeyResponse
import pl.edu.agh.bioauth.apigateway.service.common.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.common.ErrorService
import pl.edu.agh.bioauth.apigateway.service.common.SecurityService
import pl.edu.agh.bioauth.apigateway.util.extension.path
import pl.edu.agh.bioauth.apigateway.util.extension.stringValue
import pl.edu.agh.bioauth.apigateway.util.extension.toPublicKey
import javax.servlet.http.HttpServletRequest

@Service
class EncryptionService(private val databaseService: DatabaseService,
                        private val securityService: SecurityService,
                        private val errorService: ErrorService,
                        private val request: HttpServletRequest) {

    @Throws(RequestException::class)
    fun getSymmetricKey(appId: String, appSecret: String, publicKey: String): SymmetricKeyResponse {
        databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound(request.path)

        val symmetricKey = securityService.getSymmetricKey()
        databaseService.saveEnryptionKey(EncryptionKey(symmetricKey.stringValue)).run {
            val encryptedKey = securityService.encryptString(value, publicKey.toPublicKey())
            return SymmetricKeyResponse(id, encryptedKey)
        }
    }
}