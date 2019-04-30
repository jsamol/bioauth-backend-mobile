package pl.edu.agh.bioauth.apigateway.service.helper

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.InternalFailureException
import pl.edu.agh.bioauth.apigateway.exception.ServiceFailureException

@Service
class ErrorService {
    fun failWithAppNotFound(): Nothing = throw AppNotFoundException()

    fun failWithServiceError(status: HttpStatus): Nothing = throw ServiceFailureException(status)

    fun failWithAuthenticationError(): Nothing = throw AuthenticationFailedException()

    fun failWithInternalError(): Nothing = throw InternalFailureException()
}