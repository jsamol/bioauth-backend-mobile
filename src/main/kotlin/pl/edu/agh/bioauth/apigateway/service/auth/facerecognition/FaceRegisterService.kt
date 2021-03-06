package pl.edu.agh.bioauth.apigateway.service.auth.facerecognition

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.RequestException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.auth.RegisterResponse
import pl.edu.agh.bioauth.apigateway.service.auth.RegisterService
import pl.edu.agh.bioauth.apigateway.util.constant.AuthQualifier.FACE_RECOGNITION

@Service
@Qualifier(FACE_RECOGNITION)
class FaceRegisterService : RegisterService() {

    @Throws(RequestException::class)
    override fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String, keyId: String): RegisterResponse =
            saveBiometricPattern(samples, appId, appSecret, userId, keyId, BiometricPattern.Type.FACE)
}