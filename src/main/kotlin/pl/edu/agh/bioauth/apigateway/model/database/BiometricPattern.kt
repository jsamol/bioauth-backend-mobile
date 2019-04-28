package pl.edu.agh.bioauth.apigateway.model.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class BiometricPattern(val filePaths: List<String>,
                            val appId: ObjectId,
                            val userId: String,
                            val privateKey: String,
                            val type: Type) {
    @Id
    private var _id: ObjectId? = null

    enum class Type {
        FACE
    }
}