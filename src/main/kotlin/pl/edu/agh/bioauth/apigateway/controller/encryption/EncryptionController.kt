package pl.edu.agh.bioauth.apigateway.controller.encryption

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.bioauth.apigateway.controller.ApiController
import pl.edu.agh.bioauth.apigateway.controller.ApiController.Companion.API_URI
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.service.encryption.EncryptionService
import pl.edu.agh.bioauth.apigateway.util.constant.ApiRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.constant.ApiRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.constant.ApiRequestParam.PUBLIC_KEY

@RestController
@RequestMapping("$API_URI/encryption")
class EncryptionController(private val encryptionService: EncryptionService) : ApiController() {

    @RequestMapping("/session-key", method = [RequestMethod.GET])
    fun getSessionKey(@RequestParam(name = APP_ID, required = true) appId: String,
                      @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                      @RequestParam(name = PUBLIC_KEY, required = true) publicKey: String): ResponseEntity<ApiResponse> =
            getResponseEntity { encryptionService.getSymmetricKey(appId, appSecret, publicKey) }
}