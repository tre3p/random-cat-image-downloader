package com.tre3p.randomizedjpgdownloader.listener

import com.tre3p.randomizedjpgdownloader.service.ImageDownloadingProcessorService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(private val imageDownloadingProcessorService: ImageDownloadingProcessorService) {

    private val coroutinesCount = Runtime.getRuntime().availableProcessors() * 2;

    @EventListener(ApplicationReadyEvent::class)
    fun launchImagesDownloading() {
        runBlocking { imageDownloadingProcessorService.launchImageDownloading(coroutinesCount) }
    }
}