package pl.edu.agh.bioauth.apigateway.service.common

import org.json.JSONObject
import org.springframework.stereotype.Service

@Service
class MetadataService {

    fun wasLivenessTested(metadataBytes: ByteArray): Boolean {
        metadataBytes.inputStream().bufferedReader().use {
            val metadata = JSONObject(it.readText())
            return if (metadata.isNull(METADATA_LIVENESS_STATUS)) false else metadata.getBoolean(METADATA_LIVENESS_STATUS)
        }
    }

    companion object {
        private const val METADATA_LIVENESS_STATUS = "livenessStatus"
    }
}