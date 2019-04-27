package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.model.network.RegisterResponse
import pl.edu.agh.bioauth.apigateway.service.AuthService
import pl.edu.agh.bioauth.apigateway.util.AuthContentType.MULTIPART
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_SECRET
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
                 @RequestParam(name = USER_ID, required = true) userId: String): RegisterResponse =
            authService.registerPattern(samples, appId, appSecret, userId)

}