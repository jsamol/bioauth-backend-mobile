package pl.edu.agh.bioauth.apigateway.service.helper

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.agh.bioauth.apigateway.util.extension.save
import pl.edu.agh.bioauth.apigateway.util.extension.saveTemp

@Service
class FileService {
    fun saveSamples(samples: List<MultipartFile>, temp: Boolean = false): List<String> =
            samples.map(if (temp) MultipartFile::saveTemp else MultipartFile::save)
}