package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.RecognitionFailedException
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.ErrorResponse
import pl.edu.agh.bioauth.apigateway.service.facerecognition.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.facerecognition.RegisterService
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.USER_ID

@RestController
@RequestMapping("/auth/face")
class FaceRecognitionController(private val registerService: RegisterService,
                                private val authenticateService: AuthenticateService) {

    @RequestMapping("/register", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                 @RequestParam(name = APP_ID, required = true) appId: String,
                 @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                 @RequestParam(name = USER_ID, required = true) userId: String): ResponseEntity<ApiResponse> =
            try {
                ResponseEntity.ok(registerService.registerPattern(samples, appId, appSecret, userId))
            } catch (e: AppNotFoundException) {
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.getInvalidAppCredentialsError(REGISTER_PATH))
            }

    @RequestMapping("/authenticate", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                     @RequestParam(name = APP_ID, required = true) appId: String,
                     @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                     @RequestParam(name = CHALLENGE, required = true) challenge: String): ResponseEntity<ApiResponse> =
            try {
                ResponseEntity.ok(authenticateService.authenticate(samples, appId, appSecret, challenge))
            } catch (e: AppNotFoundException) {
                ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.getInvalidAppCredentialsError(AUTHENTICATE_PATH))
            } catch (e: RecognitionFailedException) {
                ResponseEntity
                        .status(e.status)
                        .body(ErrorResponse.getRecognitionFailedError(e.status, AUTHENTICATE_PATH))
            } catch (e: AuthenticationFailedException) {
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.getAuthenticationFailedError(AUTHENTICATE_PATH))
            }

    companion object {
        private const val REGISTER_PATH = "/auth/face/register"
        private const val AUTHENTICATE_PATH = "/auth/face/authenticate"
    }
}