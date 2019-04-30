package pl.edu.agh.bioauth.apigateway.model.network.api

import org.springframework.http.HttpStatus
import java.util.*

abstract class ErrorResponse(httpStatus: HttpStatus,
                             val error: String,
                             val message: String,
                             val path: String,
                             val timestamp: Date = Date()) : ApiResponse {

    val status: Int = httpStatus.value()

    class InvalidAppCredentials(path: String)
        : ErrorResponse(HttpStatus.BAD_REQUEST, ERROR_INVALID_APP_CREDENTIALS, ERROR_INVALID_APP_CREDENTIALS, path)

    class ServiceFailure(status: HttpStatus, path: String)
        : ErrorResponse(status, ERROR_SERVICE, ERROR_SERVICE, path)

    class AuthenticationFailure(path: String)
        : ErrorResponse(HttpStatus.UNAUTHORIZED, ERROR_UNAUTHORIZED, ERROR_UNAUTHORIZED, path)

    companion object {
        private const val ERROR_INVALID_APP_CREDENTIALS = "Invalid app credentials"
        private const val ERROR_UNAUTHORIZED = "Unauthorized"
        private const val ERROR_SERVICE = "Biometric service error"
    }
}