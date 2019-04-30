package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.service.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.RegisterService
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.USER_ID

abstract class BioAuthController(private val authenticateService: AuthenticateService,
                                 private val registerService: RegisterService) {

    @RequestMapping("/register", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                 @RequestParam(name = APP_ID, required = true) appId: String,
                 @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                 @RequestParam(name = USER_ID, required = true) userId: String): ResponseEntity<ApiResponse> =
            getResponseEntity { registerService.register(samples, appId, appSecret, userId) }

    @RequestMapping("/authenticate", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                     @RequestParam(name = APP_ID, required = true) appId: String,
                     @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                     @RequestParam(name = CHALLENGE, required = true) challenge: String): ResponseEntity<ApiResponse> =
            getResponseEntity { authenticateService.authenticate(samples, appId, appSecret, challenge) }

    private inline fun getResponseEntity(serviceMethod: () -> ApiResponse): ResponseEntity<ApiResponse> =
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
        const val AUTH_URI = "/auth"
    }
}