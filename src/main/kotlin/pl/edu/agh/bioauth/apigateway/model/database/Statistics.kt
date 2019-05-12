package pl.edu.agh.bioauth.apigateway.model.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class Statistics(
        val appId: ObjectId,
        val batteryLevel: Float,
        val isCharging: Boolean,
        val chargeType: Int,
        val connectionType: Int,
        val availableMemory: Float,
        val lowMemory: Boolean,
        val batteryDrain: Float,
        val executionTime: Long
) {
    @Id
    private var _id: ObjectId? = null

    val id: String
        get() = _id?.toHexString() ?: ""
}