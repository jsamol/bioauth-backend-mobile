package pl.edu.agh.bioauth.apigateway.util.extension

import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.service.common.FileService
import java.io.File

fun List<MultipartFile>.getMetadata(): MultipartFile? =
        find { it.originalFilename?.contains(FileService.FileType.JSON) == true }

fun List<MultipartFile>.getSamples(): List<MultipartFile> =
        filterNot { it.originalFilename?.contains(FileService.FileType.JSON) == true }

fun List<File>.getPaths(): List<String> = map { it.absolutePath }

fun List<File>.deleteAll() = forEach { it.delete() }