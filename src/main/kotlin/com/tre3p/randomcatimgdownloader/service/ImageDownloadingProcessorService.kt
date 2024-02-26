package com.tre3p.randomcatimgdownloader.service

import com.tre3p.randomcatimgdownloader.dto.ImageDto
import com.tre3p.randomcatimgdownloader.entity.ImageData
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Paths
import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class ImageDownloadingProcessorService(
    private val imageSaverService: ImageDbSaverService,
    private val imageStatService: ImageStatService,
    private val imageDownloaderService: ImageDownloaderService,
    private val imageDiskSaverService: ImageDiskSaverService
) {
    companion object ImageConstants {
        private const val IMAGE_DOWNLOAD_URL_TEMPLATE = "https://loremflickr.com/%s/%s"
        private const val IMAGE_SIZE_LOWER_BOUND = 10
        private const val IMAGE_SIZE_UPPER_BOUND = 5000
    }

    @Lazy
    @Autowired
    private lateinit var imgProcessorService: ImageDownloadingProcessorService

    @Value("\${image.directory}")
    private lateinit var imageSaveDirectory: String

    private val log = KotlinLogging.logger {}

    suspend fun launchImageDownloading(coroutinesCount: Int) = coroutineScope {
        log.info { "launchImageDownloading(): launching images downloading. coroutinesCount: $coroutinesCount" }
        repeat(coroutinesCount) {
            launch { startImagesProcessing() }
        }
        log.info { "launchImageDownloading(): all coroutines are started" }
    }

   private suspend fun startImagesProcessing() {
        while (true) {
            val randomImageUrl = generateRandomImageDownloadUrl()
            withContext(Dispatchers.IO) {
                var imageResponse: ImageDto? = null

                try {
                    imageResponse = imageDownloaderService.downloadImage(randomImageUrl)
                } catch (e: Exception) {
                    log.warn { "startImagesProcessing(): error while downloading image: ${e.message}" }
                }

                if (imageResponse != null) {
                    imgProcessorService.processImage(imageResponse)
                }
            }
        }
    }

    @Transactional
    suspend fun processImage(imgDto: ImageDto) {
        val imageFileName = generateRandomImageFileName()
        val imageRelativePath = if (imageSaveDirectory.endsWith("/")) "$imageSaveDirectory$imageFileName" else "$imageSaveDirectory/$imageFileName"
        val imageFilePath = Paths.get(imageRelativePath).toAbsolutePath().toString()

        imageDiskSaverService.saveImageToDisk(imageFilePath, imgDto.imageBytes)
        val imgData = ImageData(imgDto.imageSizeKb, imgDto.imageContentType, imageFilePath, imgDto.downloadUrl)

        imageSaverService.saveImageData(imgData)
        imageStatService.updateImageStat(imgDto)
    }

    private fun generateRandomImageDownloadUrl(): String {
        val width = Random.nextInt(IMAGE_SIZE_LOWER_BOUND..IMAGE_SIZE_UPPER_BOUND)
        val height = Random.nextInt(IMAGE_SIZE_LOWER_BOUND..IMAGE_SIZE_UPPER_BOUND)

        return String.format(IMAGE_DOWNLOAD_URL_TEMPLATE, width, height)
    }

    private fun generateRandomImageFileName(): String = "${UUID.randomUUID()}.jpeg"
}