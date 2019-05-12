package pl.edu.agh.bioauth.apigateway.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import pl.edu.agh.bioauth.apigateway.model.database.EncryptionKey

interface EncryptionKeyRepository : MongoRepository<EncryptionKey, ObjectId>