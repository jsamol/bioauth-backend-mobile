package pl.edu.agh.bioauth.apigateway.controller.auth.facerecognition

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.bioauth.apigateway.controller.auth.AuthController
import pl.edu.agh.bioauth.apigateway.controller.auth.AuthController.Companion.AUTH_URI
import pl.edu.agh.bioauth.apigateway.service.auth.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.auth.RegisterService
import pl.edu.agh.bioauth.apigateway.util.constant.AuthQualifier.FACE_RECOGNITION

@RestController
@RequestMapping("$AUTH_URI/face")
class FaceController(
        @Qualifier(FACE_RECOGNITION) authenticateService: AuthenticateService,
        @Qualifier(FACE_RECOGNITION) registerService: RegisterService
) : AuthController(authenticateService, registerService)