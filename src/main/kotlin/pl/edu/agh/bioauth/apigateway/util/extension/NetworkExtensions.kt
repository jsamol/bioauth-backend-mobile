package pl.edu.agh.bioauth.apigateway.util.extension

import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

val HttpServletRequest.path: String
    get() = getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()