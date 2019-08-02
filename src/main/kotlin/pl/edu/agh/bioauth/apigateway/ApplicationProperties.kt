package pl.edu.agh.bioauth.apigateway

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern.Type.FACE

@Component
@ConfigurationProperties(prefix = "bioauth")
class ApplicationProperties {

    private val faceRecognitionServicePath: String? by lazy { service["face-recognition"]?.get("uri") }

    val biometricMethodsPaths: Map<BiometricPattern.Type, String> by lazy { mapOf(
            FACE to "$faceRecognitionServicePath/face/identification"
    ) }

    val patternExtractionPaths: Map<BiometricPattern.Type, String> by lazy { mapOf(
            FACE to "$faceRecognitionServicePath/face/encodings"
    ) }

    lateinit var service: Map<String, Map<String, String>>
}