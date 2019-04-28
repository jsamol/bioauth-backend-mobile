package pl.edu.agh.bioauth.apigateway.service.facerecognition

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.exception.AuthenticationFailedException
import pl.edu.agh.bioauth.apigateway.exception.ServiceFailureException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.AuthenticateResponse
import pl.edu.agh.bioauth.apigateway.service.AuthenticateService
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthQualifier.FACE_RECOGNITION

@Service
@Qualifier(FACE_RECOGNITION)
class FaceAuthenticateService : AuthenticateService() {

    @Throws(AppNotFoundException::class, ServiceFailureException::class, AuthenticationFailedException::class)
    override fun authenticate(samples: List<MultipartFile>, appId: String, appSecret: String, challenge: String): AuthenticateResponse =
            recognizeSamples(samples, appId, appSecret, challenge, BiometricPattern.Type.FACE)
}