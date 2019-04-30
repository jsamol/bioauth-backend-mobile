package pl.edu.agh.bioauth.apigateway.exception

import org.springframework.http.HttpStatus

class ServiceFailureException(val status: HttpStatus) : Exception()