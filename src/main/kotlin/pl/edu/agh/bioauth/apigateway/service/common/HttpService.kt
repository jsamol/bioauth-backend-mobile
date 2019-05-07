package pl.edu.agh.bioauth.apigateway.service.common

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.edu.agh.bioauth.apigateway.ApplicationProperties
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern
import pl.edu.agh.bioauth.apigateway.model.network.service.request.ServiceRequest
import pl.edu.agh.bioauth.apigateway.model.network.service.response.ServiceResponse
import kotlin.reflect.KClass

@Service
class HttpService(private val applicationProperties: ApplicationProperties,
                  restTemplateBuilder: RestTemplateBuilder) {

    private val restTemplate: RestTemplate by lazy { restTemplateBuilder.build() }

    fun <Req : ServiceRequest, Res : ServiceResponse>
            post(path: String,
                 request: Req,
                 responseType: KClass<Res>,
                 contentType: MediaType = MediaType.APPLICATION_JSON): ResponseEntity<Res> {
        val requestHeaders = HttpHeaders().also { it.contentType = contentType }
        val requestEntity = HttpEntity(request, requestHeaders)

        return restTemplate.postForEntity(path, requestEntity, responseType.java)
    }

    fun getBiometricServicePath(patternType: BiometricPattern.Type): String? =
            applicationProperties.biometricMethodsPaths[patternType]
}