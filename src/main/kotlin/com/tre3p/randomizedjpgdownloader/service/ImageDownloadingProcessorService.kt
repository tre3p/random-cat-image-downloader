package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class ImageDownloadingProcessorService(
    private val imageSaverService: ImageSaverService,
    private val imageStatService: ImageStatService,
    private val imageDownloaderService: ImageDownloaderService
) {
    companion object ImageConstants {
        private const val IMAGE_DOWNLOAD_URL_TEMPLATE = "https://loremflickr.com/%s/%s"
        private const val IMAGE_SIZE_LOWER_BOUND = 10
        private const val IMAGE_SIZE_UPPER_BOUND = 5000
    }

    @Autowired
    private lateinit var img: ImageDownloadingProcessorService

    private val log = KotlinLogging.logger {}

    suspend fun launchImageDownloading(coroutinesCount: Int) = coroutineScope {
        log.info { "launchImageDownloading(): launching images downloading" }
        repeat(coroutinesCount) {
            launch { startImagesProcessing() }
        }
        log.info { "launchImageDownloading(): all coroutines are started" }
    }

   private suspend fun startImagesProcessing() = coroutineScope {
        while (true) {
            val randomImageUrl = generateRandomImageDownloadUrl()
            val imageResponse = imageDownloaderService.downloadImage(randomImageUrl)
            launch { img.processImage(imageResponse) }
        }
    }

    //@Transactional
    // TODO: marking this method as @Transactional leads to NoClassDefFoundError on reactivestreams library
    fun processImage(imgData: ImageData) {
        imageSaverService.saveImage(imgData)
        imageStatService.updateImageStat(imgData)
    }

    private fun generateRandomImageDownloadUrl(): String {
        val width = Random.nextInt(IMAGE_SIZE_LOWER_BOUND..IMAGE_SIZE_UPPER_BOUND)
        val height = Random.nextInt(IMAGE_SIZE_LOWER_BOUND..IMAGE_SIZE_UPPER_BOUND)

        return String.format(IMAGE_DOWNLOAD_URL_TEMPLATE, width, height)
    }
}