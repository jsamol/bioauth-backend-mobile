package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.ServiceFailureException
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.ErrorResponse
import pl.edu.agh.bioauth.apigateway.service.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.RegisterService
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.USER_ID

abstract class BioAuthController(private val authenticateService: AuthenticateService,
                                 private val registerService: RegisterService) {

    abstract fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                          @RequestParam(name = APP_ID, required = true) appId: String,
                          @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                          @RequestParam(name = USER_ID, required = true) userId: String): ResponseEntity<ApiResponse>

    abstract fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                              @RequestParam(name = APP_ID, required = true) appId: String,
                              @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                              @RequestParam(name = CHALLENGE, required = true) challenge: String): ResponseEntity<ApiResponse>

    protected fun registerSamples(samples: List<MultipartFile>,
                                  appId: String,
                                  appSecret: String,
                                  userId: String,
                                  registerPath: String): ResponseEntity<ApiResponse> {
        return try {
            ResponseEntity.ok(registerService.register(samples, appId, appSecret, userId))
        } catch (e: AppNotFoundException) {
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.getInvalidAppCredentialsError(registerPath))
        }
    }

    protected fun authenticateSamples(samples: List<MultipartFile>,
                                      appId: String,
                                      appSecret: String,
                                      challenge: String,
                                      authenticatePath: String): ResponseEntity<ApiResponse> {
        return try {
            ResponseEntity.ok(authenticateService.authenticate(samples, appId, appSecret, challenge))
        } catch (e: AppNotFoundException) {
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.getInvalidAppCredentialsError(authenticatePath))
        } catch (e: ServiceFailureException) {
            ResponseEntity
                    .status(e.status)
                    .body(ErrorResponse.getRecognitionFailedError(e.status, authenticatePath))
        } catch (e: AuthenticationFailedException) {
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.getAuthenticationFailedError(authenticatePath))
        }
    }
}