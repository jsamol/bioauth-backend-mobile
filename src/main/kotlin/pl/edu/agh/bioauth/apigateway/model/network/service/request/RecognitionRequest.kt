package pl.edu.agh.bioauth.apigateway.model.network.service.request

data class RecognitionRequest(val samples: List<String>, val patterns: Map<String, List<String>>) : ServiceRequest