package pl.edu.agh.bioauth.apigateway.exception

import java.lang.Exception

class RecognitionFailedException(val status: Int) : Exception()