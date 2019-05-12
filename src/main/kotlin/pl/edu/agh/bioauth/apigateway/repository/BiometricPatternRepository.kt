package pl.edu.agh.bioauth.apigateway.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import pl.edu.agh.bioauth.apigateway.model.database.BiometricPattern

interface BiometricPatternRepository : MongoRepository<BiometricPattern, ObjectId> {
    fun findByAppId(appId: ObjectId): List<BiometricPattern>
    fun findByAppIdAndUserId(appId: ObjectId, userId: String): List<BiometricPattern>
}