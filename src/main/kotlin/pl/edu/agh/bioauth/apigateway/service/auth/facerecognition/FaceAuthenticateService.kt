package pl.edu.agh.bioauth.apigateway.service.auth.facerecognition

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.auth.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.service.auth.AuthenticateService
import pl.edu.agh.bioauth.apigateway.util.constant.AuthQualifier.FACE_RECOGNITION

@Service
@Qualifier(FACE_RECOGNITION)
class FaceAuthenticateService : AuthenticateService() {

    @Throws(RequestException::class)
    override fun authenticate(samples: List<MultipartFile>, appId: String, appSecret: String, challenge: String): AuthenticateResponse =
            recognizeSamples(samples, appId, appSecret, challenge, BiometricPattern.Type.FACE)
}