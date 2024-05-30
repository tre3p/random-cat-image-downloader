package com.tre3p.randomcatimgdownloader.service

import com.tre3p.randomcatimgdownloader.dto.ImageDto
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import org.springframework.stereotype.Service

@Service
class ImageDownloaderService(private val httpClient: HttpClient) {

    private val log = KotlinLogging.logger {}

    suspend fun downloadImage(url: String): ImageDto? {
        return try {
            httpClient.get(url).let {
                convertImageResponseToImageDto(it, url)
            }
        } catch (e: Exception) {
            log.warn { "downloadImage(): error while downloading image: $e" }
            null
        }
    }

    private suspend fun convertImageResponseToImageDto(imgResponse: HttpResponse, downloadUrl: String): ImageDto {
        val contentType = imgResponse.headers.get("Content-Type") ?: ""
        val content = imgResponse.body<ByteArray>()
        val contentSize = content.sizeInKilobytes()

        return ImageDto(content, downloadUrl, contentType, contentSize)
    }

    fun ByteArray.sizeInKilobytes(): Double = (this.size / 1024.0)
}