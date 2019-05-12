package pl.edu.agh.bioauth.apigateway.util.extension

import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.util.FileManager
import java.io.File

fun MultipartFile.save(): File = FileManager.createFile(originalFilename).also { transferTo(it) }

fun MultipartFile.saveTemp(): File = FileManager.createTempFile(originalFilename).also { transferTo(it) }

fun List<MultipartFile>.saveAll(temp: Boolean = false): List<File> = map(if (temp) MultipartFile::saveTemp else MultipartFile::save)

fun List<File>.getPaths(): List<String> = map { it.absolutePath }

fun List<File>.deleteAll() = forEach { it.delete() }