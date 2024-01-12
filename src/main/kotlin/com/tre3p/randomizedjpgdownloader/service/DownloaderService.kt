package com.tre3p.randomizedjpgdownloader.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class DownloaderService(private val restTemplate: RestTemplate) {
    fun download(url: String): ByteArray {
        val response = restTemplate.getForEntity(url, ByteArray::class.java)
        return response.body ?: throw RestClientException("Response body is null")
    }
}