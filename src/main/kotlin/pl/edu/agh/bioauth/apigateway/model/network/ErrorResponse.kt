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
        private const val ERROR_FACE_RECOGNITION = "Face Recognition Error"

        fun getInvalidAppCredentialsError(path: String): ErrorResponse =
                ErrorResponse(Date(), HttpStatus.UNAUTHORIZED.value(), ERROR_UNAUTHORIZED, ERROR_UNAUTHORIZED, path)

        fun getFaceRecognitionFailedError(status: Int, path: String): ErrorResponse =
                ErrorResponse(Date(), status, ERROR_FACE_RECOGNITION, ERROR_FACE_RECOGNITION, path)
    }
}