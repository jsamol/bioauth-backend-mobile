package pl.edu.agh.bioauth.apigateway.exception

import java.lang.Exception

class ServiceFailureException(val status: Int) : Exception()