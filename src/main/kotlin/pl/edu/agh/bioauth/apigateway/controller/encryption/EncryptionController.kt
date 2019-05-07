package pl.edu.agh.bioauth.apigateway.controller.encryption

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.bioauth.apigateway.controller.ApiController
import pl.edu.agh.bioauth.apigateway.controller.ApiController.Companion.API_URI
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.encryption.SymmetricKeyRequest
import pl.edu.agh.bioauth.apigateway.service.encryption.EncryptionService

@RestController
@RequestMapping("$API_URI/encryption")
class EncryptionController(private val encryptionService: EncryptionService) : ApiController() {

    @RequestMapping("/symmetricKey", method = [RequestMethod.POST])
    fun getSymmetricKey(@RequestBody request: SymmetricKeyRequest): ResponseEntity<ApiResponse> =
            getResponseEntity { with (request) {
                encryptionService.getSymmetricKey(appId, appSecret, publicKey)
            } }
}