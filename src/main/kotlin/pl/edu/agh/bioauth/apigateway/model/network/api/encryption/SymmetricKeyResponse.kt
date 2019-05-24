package pl.edu.agh.bioauth.apigateway.model.network.api.encryption

import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse

data class SymmetricKeyResponse(val id: String, val key: String, val iv: String) : ApiResponse