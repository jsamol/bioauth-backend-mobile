package pl.edu.agh.bioauth.apigateway.exception

import java.lang.Exception

class FaceRecognitionFailedException(val status: Int) : Exception()