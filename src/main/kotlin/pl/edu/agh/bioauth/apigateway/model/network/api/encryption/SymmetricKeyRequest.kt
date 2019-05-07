package pl.edu.agh.bioauth.apigateway.model.network.api.encryption

import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse

data class SymmetricKeyRequest(val appId: String,
                               val appSecret: String,
                               val publicKey: String) : ApiResponse