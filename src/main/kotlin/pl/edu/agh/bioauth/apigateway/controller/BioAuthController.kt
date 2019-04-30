package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.controller.facerecognition.FaceController
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.InternalFailureException
import pl.edu.agh.bioauth.apigateway.exception.ServiceFailureException
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.ErrorResponse
import pl.edu.agh.bioauth.apigateway.service.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.RegisterService
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.USER_ID
import pl.edu.agh.bioauth.apigateway.util.extension.path
import javax.servlet.http.HttpServletRequest

abstract class BioAuthController(private val authenticateService: AuthenticateService,
                                 private val registerService: RegisterService) {

    @RequestMapping("/register", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                 @RequestParam(name = APP_ID, required = true) appId: String,
                 @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                 @RequestParam(name = USER_ID, required = true) userId: String,
                 request: HttpServletRequest): ResponseEntity<ApiResponse> {
        return try {
            ResponseEntity.ok(registerService.register(samples, appId, appSecret, userId))
        } catch (e: AppNotFoundException) {
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.InvalidAppCredentials(request.path))
        }
    }

    @RequestMapping("/authenticate", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                     @RequestParam(name = APP_ID, required = true) appId: String,
                     @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                     @RequestParam(name = CHALLENGE, required = true) challenge: String,
                     request: HttpServletRequest): ResponseEntity<ApiResponse> {
        return try {
            ResponseEntity.ok(authenticateService.authenticate(samples, appId, appSecret, challenge))
        } catch (e: AppNotFoundException) {
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.InvalidAppCredentials(request.path))
        } catch (e: ServiceFailureException) {
            ResponseEntity
                    .status(e.status)
                    .body(ErrorResponse.ServiceFailure(e.status, request.path))
        } catch (e: AuthenticationFailedException) {
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.AuthenticationFailure(request.path))
        } catch (e: InternalFailureException) {
            ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.InternalFailure(request.path))
        }
    }

    companion object {
        const val AUTH_URI = "/auth"
    }
}