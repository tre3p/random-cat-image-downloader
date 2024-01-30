package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ImageDownloaderService(private val restTemplate: RestTemplate) {

    suspend fun downloadImage(url: String): ImageData {
        val response = restTemplate.getForEntity(url, ByteArray::class.java)
        return convertImageResponseToImageData(response, url)
    }

    private fun convertImageResponseToImageData(imgResponse: ResponseEntity<ByteArray>, downloadUrl: String): ImageData {
        val contentType = imgResponse.headers["Content-Type"]?.first().orEmpty()
        val content = imgResponse.body ?: byteArrayOf()
        val contentSize = content.sizeInKilobytes()

        return ImageData(contentSize, contentType, content, downloadUrl)
    }

    fun ByteArray.sizeInKilobytes(): Double = (this.size / 1024.0)
}