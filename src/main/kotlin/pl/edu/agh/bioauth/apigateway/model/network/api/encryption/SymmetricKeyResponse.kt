package pl.edu.agh.bioauth.apigateway.model.network.api.encryption

import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse

data class SymmetricKeyResponse(val key: String) : ApiResponse