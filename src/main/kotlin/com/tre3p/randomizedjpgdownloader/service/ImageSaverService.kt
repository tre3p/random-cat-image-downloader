package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import com.tre3p.randomizedjpgdownloader.repository.ImageRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ImageSaverService(private val imageRepository: ImageRepository) {

    private val log = KotlinLogging.logger {}

    fun saveImage(imageData: ImageData) {
        log.debug { "+saveImage(): saving image" }
        imageRepository.save(imageData)
        log.debug { "-saveImage(): image saved" }
    }
}