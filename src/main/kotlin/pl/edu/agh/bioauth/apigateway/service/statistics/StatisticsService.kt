package pl.edu.agh.bioauth.apigateway.service.statistics

import org.springframework.stereotype.Service
import pl.edu.agh.bioauth.apigateway.model.database.Statistics
import pl.edu.agh.bioauth.apigateway.model.network.api.EmptyResponse
import pl.edu.agh.bioauth.apigateway.model.network.api.statistics.StatsRequest
import pl.edu.agh.bioauth.apigateway.service.common.DatabaseService
import pl.edu.agh.bioauth.apigateway.service.common.ErrorService
import pl.edu.agh.bioauth.apigateway.util.extension.path
import javax.servlet.http.HttpServletRequest

@Service
class StatisticsService(private val databaseService: DatabaseService,
                        private val errorService: ErrorService,
                        private val request: HttpServletRequest) {

    fun saveStatistics(statsRequest: StatsRequest): EmptyResponse {
        with (statsRequest) {
            val app = databaseService.getApp(appId, appSecret) ?: errorService.failWithAppNotFound(request.path)
            databaseService.saveStatistics(Statistics(
                    app._id,
                    batteryLevel,
                    isCharging,
                    chargeType,
                    connectionType,
                    availableMemory,
                    lowMemory,
                    batteryDrain,
                    executionTime
            ))
        }

        return EmptyResponse()
    }
}