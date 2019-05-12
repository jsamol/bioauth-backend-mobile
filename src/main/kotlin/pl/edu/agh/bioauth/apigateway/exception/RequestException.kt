package pl.edu.agh.bioauth.apigateway.exception

import pl.edu.agh.bioauth.apigateway.model.network.api.ErrorResponse

data class RequestException(val response: ErrorResponse) : Throwable()