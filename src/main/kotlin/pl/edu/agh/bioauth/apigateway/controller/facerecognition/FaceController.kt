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
import pl.edu.agh.bioauth.apigateway.controller.facerecognition.FaceController.Companion.CONTROLLER_PATH
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.service.AuthenticateService
import pl.edu.agh.bioauth.apigateway.service.RegisterService
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.APP_ID
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.APP_SECRET
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.CHALLENGE
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.SAMPLES
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthRequestParam.USER_ID
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthQualifier.FACE_RECOGNITION

@RestController
@RequestMapping(CONTROLLER_PATH)
class FaceController(
        @Qualifier(FACE_RECOGNITION) authenticateService: AuthenticateService,
        @Qualifier(FACE_RECOGNITION) registerService: RegisterService
) : BioAuthController(authenticateService, registerService) {

    @RequestMapping(REGISTER_PATH, method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun register(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                          @RequestParam(name = APP_ID, required = true) appId: String,
                          @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                          @RequestParam(name = USER_ID, required = true) userId: String): ResponseEntity<ApiResponse> =
            registerSamples(samples, appId, appSecret, userId, FULL_REGISTER_PATH)

    @RequestMapping(AUTHENTICATE_PATH, method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun authenticate(@RequestParam(name = SAMPLES, required = true) samples: List<MultipartFile>,
                              @RequestParam(name = APP_ID, required = true) appId: String,
                              @RequestParam(name = APP_SECRET, required = true) appSecret: String,
                              @RequestParam(name = CHALLENGE, required = true) challenge: String): ResponseEntity<ApiResponse> =
            authenticateSamples(samples, appId, appSecret, challenge, FULL_AUTHENTICATE_PATH)

    companion object {
        const val CONTROLLER_PATH = "/auth/face"

        const val REGISTER_PATH = "/register"
        const val AUTHENTICATE_PATH = "/authenticate"

        private const val FULL_REGISTER_PATH = "$CONTROLLER_PATH/$REGISTER_PATH"
        private const val FULL_AUTHENTICATE_PATH = "$CONTROLLER_PATH/$AUTHENTICATE_PATH"
    }
}