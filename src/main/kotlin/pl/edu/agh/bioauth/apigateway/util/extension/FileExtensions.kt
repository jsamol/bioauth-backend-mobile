package pl.edu.agh.bioauth.apigateway.util.extension

import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.util.FileManager

fun MultipartFile.save(): String = FileManager.createFile(originalFilename).also { transferTo(it) }.absolutePath

fun MultipartFile.saveTemp(): String = FileManager.createTempFile(originalFilename).also { transferTo(it) }.absolutePath