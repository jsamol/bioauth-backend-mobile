package pl.edu.agh.bioauth.apigateway.model.network.api

import org.springframework.http.HttpStatus
import java.util.*

class ErrorResponse(val timestamp: Date,
                    val status: Int,
                    val error: String,
                    val message: String,
                    val path: String) : ApiResponse {

    companion object {
        private const val ERROR_INVALID_APP_CREDENTIALS = "Invalid App Credentials"
        private const val ERROR_UNAUTHORIZED = "Unauthorized"
        private const val ERROR_RECOGNITION = "Recognition Error"

        fun getInvalidAppCredentialsError(path: String): ErrorResponse =
                ErrorResponse(Date(), HttpStatus.BAD_REQUEST.value(), ERROR_INVALID_APP_CREDENTIALS, ERROR_INVALID_APP_CREDENTIALS, path)

        fun getRecognitionFailedError(status: Int, path: String): ErrorResponse =
                ErrorResponse(Date(), status, ERROR_RECOGNITION, ERROR_RECOGNITION, path)

        fun getAuthenticationFailedError(path: String): ErrorResponse =
                ErrorResponse(Date(), HttpStatus.UNAUTHORIZED.value(), ERROR_UNAUTHORIZED, ERROR_UNAUTHORIZED, path)
    }
}