package pl.edu.agh.bioauth.apigateway.model.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class App(@Id val _id: ObjectId,
               val userId: String,
               val name: String,
               val appId: String,
               val appSecret: String,
               val description: String?)