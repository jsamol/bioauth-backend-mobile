package pl.edu.agh.bioauth.apigateway.controller.facerecognition

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.controller.BioAuthController
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.service.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.RegisterService
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.AuthRequestParam.USER_ID
import pl.edu.agh.bioauth.apigateway.util.Qualifier.FACE_RECOGNITION

@RestController
@RequestMapping("/auth/face")
class FaceController(
        @Qualifier(FACE_RECOGNITION) authenticateService: AuthenticateService,
        @Qualifier(FACE_RECOGNITION) registerService: RegisterService
) : BioAuthController(authenticateService, registerService) {

    @RequestMapping("/register", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                          @RequestParam(name = APP_ID, required = true) appId: String,
                          @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                          @RequestParam(name = USER_ID, required = true) userId: String): ResponseEntity<ApiResponse> =
            registerSamples(samples, appId, appSecret, userId, REGISTER_PATH)

    @RequestMapping("/authenticate", method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                              @RequestParam(name = APP_ID, required = true) appId: String,
                              @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                              @RequestParam(name = CHALLENGE, required = true) challenge: String): ResponseEntity<ApiResponse> =
            authenticateSamples(samples, appId, appSecret, challenge, AUTHENTICATE_PATH)

    companion object {
        private const val REGISTER_PATH = "/auth/face/register"
        private const val AUTHENTICATE_PATH = "/auth/face/authenticate"
    }
}