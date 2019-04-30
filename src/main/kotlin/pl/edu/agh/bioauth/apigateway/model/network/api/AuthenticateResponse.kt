package pl.edu.agh.bioauth.apigateway.model.network.api

data class AuthenticateResponse(val userId: String, val challenge: String) : ApiResponse