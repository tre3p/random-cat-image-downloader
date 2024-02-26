package com.tre3p.randomcatimgdownloader.service

import com.tre3p.randomcatimgdownloader.dto.ImageDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ImageDownloaderService(private val restTemplate: RestTemplate) {

    suspend fun downloadImage(url: String): ImageDto {
        val response = restTemplate.getForEntity(url, ByteArray::class.java)
        return convertImageResponseToImageDto(response, url)
    }

    private fun convertImageResponseToImageDto(imgResponse: ResponseEntity<ByteArray>, downloadUrl: String): ImageDto {
        val contentType = imgResponse.headers["Content-Type"]?.first().orEmpty()
        val content = imgResponse.body ?: byteArrayOf()
        val contentSize = content.sizeInKilobytes()

        return ImageDto(content, downloadUrl, contentType, contentSize)
    }

    fun ByteArray.sizeInKilobytes(): Double = (this.size / 1024.0)
}