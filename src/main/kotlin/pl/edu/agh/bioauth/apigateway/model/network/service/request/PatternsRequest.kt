package pl.edu.agh.bioauth.apigateway.model.network.service.request

data class PatternsRequest(val samples: List<String>, val livenessStatus: Boolean, val patternDir: String) : ServiceRequest