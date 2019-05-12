package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.http.ResponseEntity
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse

abstract class ApiController {

    protected inline fun getResponseEntity(serviceMethod: () -> ApiResponse): ResponseEntity<ApiResponse> =
            try {
                ResponseEntity.ok(serviceMethod())
            } catch (e: RequestException) {
                with (e.response) {
                    ResponseEntity
                            .status(status)
                            .body(this)
                }
            }

    companion object {
        const val API_URI = "/api/v1"
    }
}