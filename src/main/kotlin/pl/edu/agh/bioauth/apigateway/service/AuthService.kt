package pl.edu.agh.bioauth.apigateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.model.network.RegisterRequest
import pl.edu.agh.bioauth.apigateway.model.network.RegisterResponse
import pl.edu.agh.bioauth.apigateway.repository.AppRepository

@Service
class AuthService {

    @Autowired
    private lateinit var appRepository: AppRepository

    fun registerPattern(registerRequest: RegisterRequest): RegisterResponse {
        return RegisterResponse("")
    }
}