package pl.edu.agh.bioauth.apigateway.model.network.api.statistics

data class StatsRequest(
        val appId: String,
        val appSecret: String,
        val batteryLevel: Float,
        val isCharging: Boolean,
        val chargeType: Int,
        val connectionType: Int,
        val availableMemory: Float,
        val lowMemory: Boolean,
        val batteryDrain: Float,
        val executionTime: Long
)