package com.tre3p.randomcatimgdownloader.service

import com.tre3p.randomcatimgdownloader.entity.ImageData
import com.tre3p.randomcatimgdownloader.repository.ImageRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ImageDbSaverService(private val imageRepository: ImageRepository) {

    private val log = KotlinLogging.logger {}

    fun saveImageData(imageData: ImageData) {
        log.debug { "+saveImageData(): saving image" }
        imageRepository.save(imageData)
        log.debug { "-saveImageData(): image saved" }
    }
}