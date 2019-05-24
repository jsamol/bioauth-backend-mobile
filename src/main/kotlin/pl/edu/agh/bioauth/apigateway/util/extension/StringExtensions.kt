package pl.edu.agh.bioauth.apigateway.util.extension

import java.util.*

fun String.decode64(): ByteArray = Base64.getDecoder().decode(this)

fun ByteArray.encode64(): String = Base64.getEncoder().encodeToString(this)