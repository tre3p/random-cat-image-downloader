package com.tre3p.randomcatimgdownloader.service

import com.tre3p.randomcatimgdownloader.dto.ImageDto
import com.tre3p.randomcatimgdownloader.entity.ImageData
import com.tre3p.randomcatimgdownloader.misc.generateRandomImageDownloadUrl
import com.tre3p.randomcatimgdownloader.misc.generateRandomImageFileName
import com.tre3p.randomcatimgdownloader.repository.ImageRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImageDownloadingProcessorService(
    private val imageRepository: ImageRepository,
    private val imageStatService: ImageStatService,
    private val imageDownloaderService: ImageDownloaderService,
    private val imageDiskSaverService: ImageDiskSaverService,
) {
    @Lazy
    @Autowired
    private lateinit var imgProcessorService: ImageDownloadingProcessorService

    private val log = KotlinLogging.logger {}

    suspend fun launchImageDownloading(coroutinesCount: Int) =
        coroutineScope {
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
                try {
                    imageDownloaderService.downloadImage(randomImageUrl).let {
                        imgProcessorService.processImage(it)
                    }
                } catch (e: Exception) {
                    log.warn { "startImagesProcessing(): error while downloading image: ${e.message}" }
                }
            }
        }
    }

    @Transactional
    suspend fun processImage(imgDto: ImageDto) {
        val imageFileName = generateRandomImageFileName()

        val savedImagePath = imageDiskSaverService.saveImageToDisk(imageFileName, imgDto.imageBytes)
        val imgData = ImageData(imgDto.imageSizeKb, imgDto.imageContentType, savedImagePath, imgDto.downloadUrl)

        imageRepository.save(imgData)
        imageStatService.updateImageStat(imgDto)
    }
}
