package pl.edu.agh.bioauth.apigateway.model.network.api.auth

import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse

data class RegisterResponse(val publicKey: String) : ApiResponse