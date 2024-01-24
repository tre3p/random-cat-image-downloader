package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import com.tre3p.randomizedjpgdownloader.entity.ImageStat
import com.tre3p.randomizedjpgdownloader.repository.FileRepository
import com.tre3p.randomizedjpgdownloader.repository.StatRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class RandomizedImageProcessorService(
    private val fileRepository: FileRepository,
    private val downloaderService: DownloaderService,
    private val statRepository: StatRepository
) {
    companion object ImageRelatedConstants {
        private const val IMAGE_DOWNLOAD_URL_TEMPLATE = "https://loremflickr.com/%s/%s"
        private const val IMAGE_SIZE_LOWER_BOUND = 10
        private const val IMAGE_SIZE_UPPER_BOUND = 5000
    }

    /**
     * Coroutines count limited to (available processors * 2) because most of the work is IO bound
     */
    private val downloaderCoroutinesCount = Runtime.getRuntime().availableProcessors() * 2

    private val log = KotlinLogging.logger {}

    private val toProcessChannel = Channel<Pair<ResponseEntity<ByteArray>, String>>(downloaderCoroutinesCount)
    private val toAnalyzeChannel = Channel<ImageData>(downloaderCoroutinesCount)

    suspend fun launchImagesProcessing() = coroutineScope {
        log.debug { "launchImagesDownloading(): launching images downloading" }
        repeat(downloaderCoroutinesCount) {
            launch { startImagesDownloading() }
        }
        launch { startImagesProcessing() }
        launch { startImagesAnalyzing() }
    }

    private suspend fun startImagesDownloading() {
        while (true) {
            val randomImageUrl = generateRandomImageDownloadUrl()
            log.debug { "downloadImages(): downloading image from url: $randomImageUrl" }

            var randomImageResponse: ResponseEntity<ByteArray>
            withContext(Dispatchers.IO) {
                try {
                    randomImageResponse = downloaderService.downloadBytes(randomImageUrl)
                    log.debug { "downloadImages(): image downloaded" }
                    toProcessChannel.send(Pair(randomImageResponse, randomImageUrl))
                } catch (e: RestClientException) {
                    log.error(e) { "downloadImages(): unable to download image" }
                }
            }
        }
    }

    private suspend fun startImagesProcessing() {
        while (true) {
            val (imgResponse, downloadUrl) = toProcessChannel.receive()
            log.debug { "startImagesProcessing(): start processing image.." }
            val imgData = convertImageResponseToImageData(imgResponse)
            imgData.downloadUrl = downloadUrl

            withContext(Dispatchers.IO) {
                try {
                    fileRepository.save(imgData)
                } catch (e: DataAccessException) {
                    log.error(e) { "startImagesProcessing(): unable to save message to DB" }
                }
            }

            toAnalyzeChannel.send(imgData)
            log.debug { "startImagesProcessing(): images processed" }
        }
    }

    private suspend fun startImagesAnalyzing() {
        val currentImageStat = statRepository.selectCurrentFileStats()
        val toSaveImageStat = ImageStat(
            currentImageStat.totalCount,
            currentImageStat.totalSize,
            LocalDateTime.now()
        )

        while (true) {
            val toAnalyzeImg = toAnalyzeChannel.receive()
            log.debug { "startImagesAnalyzing(): start analyzing image" }
            toSaveImageStat.filesCount += 1
            toSaveImageStat.filesSize += toAnalyzeImg.size

            withContext(Dispatchers.IO) {
                statRepository.save(toSaveImageStat)
            }
            log.debug { "startImagesAnalyzing(): image analyzed" }
        }
    }

    private fun convertImageResponseToImageData(imgResponse: ResponseEntity<ByteArray>): ImageData {
        val contentType = imgResponse.headers["Content-Type"]?.first().orEmpty()
        val content = imgResponse.body ?: byteArrayOf()
        val contentSize = content.sizeInKilobytes()

        return ImageData(contentSize, contentType, content)
    }

    private fun generateRandomImageDownloadUrl(): String {
        val width = Random.nextInt(IMAGE_SIZE_LOWER_BOUND..IMAGE_SIZE_UPPER_BOUND)
        val height = Random.nextInt(IMAGE_SIZE_LOWER_BOUND..IMAGE_SIZE_UPPER_BOUND)

        return String.format(IMAGE_DOWNLOAD_URL_TEMPLATE, width, height)
    }

    fun ByteArray.sizeInKilobytes(): Double = (this.size / 1024.0)
}