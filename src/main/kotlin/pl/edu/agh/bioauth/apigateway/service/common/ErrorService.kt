package pl.edu.agh.bioauth.apigateway.service.common

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.network.api.ErrorResponse

@Service
class ErrorService {

    var cleanUp: () -> Unit = {}

    fun failWithAppNotFound(path: String): Nothing =
            failWithError(ErrorResponse.InvalidAppCredentials(path))

    fun failWithServiceError(status: HttpStatus, path: String, message: String? = null): Nothing =
            failWithError(ErrorResponse.ServiceFailure(status, path, message))

    fun failWithRegistrationError(path: String): Nothing =
            failWithError(ErrorResponse.RegistrationFailure(path))

    fun failWithAuthenticationError(path: String): Nothing =
            failWithError(ErrorResponse.AuthenticationFailure(path))

    fun failWithInternalError(path: String): Nothing =
            failWithError(ErrorResponse.InternalFailure(path))

    private fun failWithError(response: ErrorResponse): Nothing {
        cleanUp()
        throw RequestException(response)
    }
}