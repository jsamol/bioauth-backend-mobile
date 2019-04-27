package pl.edu.agh.bioauth.apigateway.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.bioauth.apigateway.model.network.RegisterRequest
import pl.edu.agh.bioauth.apigateway.model.network.RegisterResponse
import pl.edu.agh.bioauth.apigateway.service.AuthService

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @RequestMapping("/register", method = [RequestMethod.POST])
    fun register(@RequestBody registerRequest: RegisterRequest): RegisterResponse =
            authService.registerPattern(registerRequest)
}