package pl.edu.agh.bioauth.apigateway.model.network.api

class AuthenticateResponse(val userId: String, val challenge: ByteArray) : ApiResponse