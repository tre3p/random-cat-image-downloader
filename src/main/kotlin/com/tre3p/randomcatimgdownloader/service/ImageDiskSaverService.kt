package com.tre3p.randomcatimgdownloader.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class ImageDiskSaverService : FileSaver {
    private val log = KotlinLogging.logger {}

    @Value("\${image.directory}")
    private lateinit var imageSaveDirectoryName: String

    private val saveDirectory by lazy { File(imageSaveDirectoryName).also { it.mkdir() } }

    override fun saveImage(fileName: String, imageBytes: ByteArray): String {
        log.debug { "+saveImageToDisk(): fileName: $fileName, content length: ${imageBytes.size}" }

        val imagePath = generateImagePath(fileName)
        File(imagePath).writeBytes(imageBytes)

        log.debug { "-saveImageToDisk() imagePath: $imagePath" }
        return imagePath
    }

    private fun generateImagePath(imageName: String): String {
        return if (saveDirectory.absolutePath.endsWith("/")) {
            "${saveDirectory.absolutePath}$imageName"
        } else {
            "${saveDirectory.absolutePath}/$imageName"
        }
    }
}
