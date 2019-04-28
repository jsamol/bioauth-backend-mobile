package pl.edu.agh.bioauth.apigateway.service.facerecognition

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.exception.AppNotFoundException
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.api.RegisterResponse
import pl.edu.agh.bioauth.apigateway.service.RegisterService
import pl.edu.agh.bioauth.apigateway.util.constant.BioAuthQualifier.FACE_RECOGNITION

@Service
@Qualifier(FACE_RECOGNITION)
class FaceRegisterService : RegisterService() {

    @Throws(AppNotFoundException::class)
    override fun register(samples: List<MultipartFile>, appId: String, appSecret: String, userId: String): RegisterResponse =
            saveBiometricPattern(samples, appId, appSecret, userId, BiometricPattern.Type.FACE)
}