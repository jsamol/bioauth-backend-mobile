package pl.edu.agh.bioauth.apigateway.model.network

data class RegisterRequest(val appId: String,
                           val appSecret: String,
                           val userId: String)