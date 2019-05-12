package pl.edu.agh.bioauth.apigateway.controller.statistics

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.bioauth.apigateway.controller.ApiController
import pl.edu.agh.bioauth.apigateway.controller.ApiController.Companion.API_URI
import pl.edu.agh.bioauth.apigateway.model.network.api.ApiResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.statistics.StatsRequest
import pl.edu.agh.bioauth.apigateway.service.statistics.StatisticsService

@RestController
@RequestMapping("$API_URI/statistics")
class StatisticsController(private val statisticsService: StatisticsService) : ApiController() {

    @RequestMapping("", method = [RequestMethod.POST])
    fun uploadStatistics(@RequestBody statsRequest: StatsRequest): ResponseEntity<ApiResponse> =
            getResponseEntity { statisticsService.saveStatistics(statsRequest) }
}