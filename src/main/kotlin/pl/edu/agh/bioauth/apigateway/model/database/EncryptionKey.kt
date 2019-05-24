package pl.edu.agh.bioauth.apigateway.model.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class EncryptionKey(val value: String, val iv: String) {
    @Id
    private var _id: ObjectId? = null

    val id: String
        get() = _id?.toHexString() ?: ""
}