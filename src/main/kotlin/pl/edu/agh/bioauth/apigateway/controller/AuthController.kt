package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.model.network.ErrorResponse
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.network.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.service.AuthService
import pl.edu.agh.bioauth.apigateway.util.AuthContentType.MULTIPART
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.USER_ID

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @RequestMapping("/register", method = [RequestMethod.POST], consumes = [MULTIPART])
    fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                 @RequestParam(name = APP_ID, required = true) appId: String,
                 @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                 @RequestParam(name = USER_ID, required = true) userId: String): ResponseEntity<ApiResponse> =
            try {
                ResponseEntity.ok(authService.registerPattern(samples, appId, appSecret, userId))
            } catch (e: AppNotFoundException) {
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.getInvalidAppCredentialsError("/auth/register"))
            }

    @RequestMapping("/authenticate", method = [RequestMethod.POST], consumes = [MULTIPART])
    fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                     @RequestParam(name = APP_ID, required = true) appId: String,
                     @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                     @RequestParam(name = CHALLENGE, required = true) challenge: String): ResponseEntity<ApiResponse> =
            try {
                ResponseEntity.ok(authService.authenticate(samples, appId, appSecret, challenge))
            } catch (e: AppNotFoundException) {
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.getInvalidAppCredentialsError("/auth/authenticate"))
            }
}