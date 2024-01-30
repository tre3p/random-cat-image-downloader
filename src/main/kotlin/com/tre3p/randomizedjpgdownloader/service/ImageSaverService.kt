package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import com.tre3p.randomizedjpgdownloader.repository.ImageRepository
import org.springframework.stereotype.Service

@Service
class ImageSaverService(private val imageRepository: ImageRepository) {
    suspend fun saveImage(imageData: ImageData) {
        imageRepository.save(imageData)
    }
}