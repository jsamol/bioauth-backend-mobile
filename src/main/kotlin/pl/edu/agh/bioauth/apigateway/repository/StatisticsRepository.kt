package pl.edu.agh.bioauth.apigateway.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import pl.edu.agh.bioauth.apigateway.model.database.Statistics

interface StatisticsRepository : MongoRepository<Statistics, ObjectId> {
}