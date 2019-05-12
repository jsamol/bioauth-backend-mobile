package pl.edu.agh.bioauth.apigateway.model.network.service.request

data class PatternsRequest(val samples: List<String>, val patternDir: String) : ServiceRequest