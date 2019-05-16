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

    class ServiceFailure(status: HttpStatus, path: String, message: String? = null)
        : ErrorResponse(status, message ?: ERROR_SERVICE, message ?: ERROR_SERVICE, path)

    class RegistrationFailure(path: String)
        : ErrorResponse(HttpStatus.BAD_REQUEST, ERROR_REGISTRATION_FAILURE, ERROR_REGISTRATION_FAILURE, path)

    class AuthenticationFailure(path: String)
        : ErrorResponse(HttpStatus.UNAUTHORIZED, ERROR_UNAUTHORIZED, ERROR_UNAUTHORIZED, path)

    class InternalFailure(path: String)
        : ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_INTERNAL, ERROR_INTERNAL, path)

    companion object {
        private const val ERROR_INVALID_APP_CREDENTIALS = "Invalid app credentials"
        private const val ERROR_REGISTRATION_FAILURE = "Could not register given samples"
        private const val ERROR_UNAUTHORIZED = "Unauthorized"
        private const val ERROR_SERVICE = "Biometric service error"
        private const val ERROR_INTERNAL = "Internal server error"
    }
}