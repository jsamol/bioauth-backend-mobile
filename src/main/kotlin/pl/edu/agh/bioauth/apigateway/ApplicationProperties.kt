package pl.edu.agh.bioauth.apigateway

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "bioauth")
class ApplicationProperties {

    val faceRecognitionPath: String by lazy { "${service["face-recognition"]?.get("uri")}/authenticate" }

    lateinit var service: Map<String, Map<String, String>>
}