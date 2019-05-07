package pl.edu.agh.bioauth.apigateway.controller.encryption

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.encryption.SymmetricKeyRequest
import pl.edu.agh.bioauth.apigateway.model.network.api.encryption.SymmetricKeyResponse

@RestController
@RequestMapping("/api/v1/encryption")
class EncryptionController {

    @RequestMapping("/symmetricKey", method = [RequestMethod.POST])
    fun getSymmetricKey(@RequestBody request: SymmetricKeyRequest): ResponseEntity<ApiResponse> {
        return ResponseEntity.ok(SymmetricKeyResponse(""))
    }
}