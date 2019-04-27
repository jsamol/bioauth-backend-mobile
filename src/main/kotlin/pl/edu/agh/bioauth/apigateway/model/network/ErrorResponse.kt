package pl.edu.agh.bioauth.apigateway.model.network

import org.springframework.http.HttpStatus
import java.util.*

class ErrorResponse(val timestamp: Date,
                    val status: Int,
                    val error: String,
                    val message: String,
                    val path: String) : ApiResponse {

    companion object {
        private const val ERROR_UNAUTHORIZED = "Unauthorized"

        fun getInvalidAppCredentialsError(path: String): ErrorResponse =
                ErrorResponse(Date(), HttpStatus.UNAUTHORIZED.value(), ERROR_UNAUTHORIZED, ERROR_UNAUTHORIZED, path)
    }
}