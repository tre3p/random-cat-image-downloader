package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.dto.ImageData
import com.tre3p.randomizedjpgdownloader.extension.sizeInKilobytes
import com.tre3p.randomizedjpgdownloader.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class RandomizedImageDownloaderService(
    @Value("\${downloader.threads.count}") private val downloaderThreadsCount: Int,
    private val fileRepository: FileRepository,
    private val downloaderService: DownloaderService
) {
    private val IMAGE_DOWNLOAD_URL_TEMPLATE = "https://loremflickr.com/%s/%s"

    suspend fun launchImagesDownloading() = coroutineScope {
        repeat(downloaderThreadsCount) {
            launch { downloadImages() }
        }
    }

    suspend fun downloadImages() = coroutineScope {
        while (true) {
            val randomImageUrl = generateRandomSizesDownloadUrl()
            withContext(Dispatchers.IO) {
                try {
                    val randomImageResponse = downloaderService.download(randomImageUrl)
                    val imageData = convertImageResponseToImageData(randomImageResponse)
                    imageData.downloadUrl = randomImageUrl
                    fileRepository.save(imageData)

                    // TODO: send to channel for analytics processing
                } catch (e: RestClientException) {
                    // TODO: log unable to download image
                } catch (e: DataAccessException) {
                    // TODO: log unable to save image to DB
                }
            }
        }
    }

    private fun convertImageResponseToImageData(imgResponse: ResponseEntity<ByteArray>): ImageData {
        val contentType = imgResponse.headers["Content-Type"]?.first()!!
        val content = imgResponse.body
        val contentSize = content?.sizeInKilobytes()!!

        return ImageData(contentSize, contentType, content)
    }

    private fun generateRandomSizesDownloadUrl(): String {
        val width = Random.nextInt(10..5000)
        val height = Random.nextInt(10..5000)

        return String.format(IMAGE_DOWNLOAD_URL_TEMPLATE, width, height)
    }
}