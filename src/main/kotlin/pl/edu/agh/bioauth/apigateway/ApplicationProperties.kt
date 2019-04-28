package pl.edu.agh.bioauth.apigateway

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern.Type.FACE

@Component
@ConfigurationProperties(prefix = "bioauth")
class ApplicationProperties {

    private val faceRecognitionPath: String by lazy { "${service["face-recognition"]?.get("uri")}/recognize" }

    val biometricMethodsPaths: Map<BiometricPattern.Type, String> by lazy { mapOf(
            FACE to faceRecognitionPath
    ) }

    lateinit var service: Map<String, Map<String, String>>
}