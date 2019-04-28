package pl.edu.agh.bioauth.apigateway.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import pl.edu.agh.bioauth.apigateway.model.database.App

interface AppRepository : MongoRepository<App, ObjectId> {
    fun findByAppIdAndAppSecret(appId: String, appSecret: String): App?
}