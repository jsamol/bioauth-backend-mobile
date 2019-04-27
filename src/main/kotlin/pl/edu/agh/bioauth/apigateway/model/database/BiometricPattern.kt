package pl.edu.agh.bioauth.apigateway.model.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class BiometricPattern(val fileIds: List<ObjectId>,
                            val appId: ObjectId,
                            val userId: String,
                            val privateKey: String) {
    @Id
    private var _id: ObjectId? = null
}