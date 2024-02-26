package com.tre3p.randomcatimgdownloader.listener

import com.tre3p.randomcatimgdownloader.service.ImageDownloadingProcessorService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(
    private val imageDownloadingProcessorService: ImageDownloadingProcessorService
) {

    /**
     * Coroutines count set to (available cores * 4) because most of the  work is IO-bound
     */
    private val coroutinesCount = Runtime.getRuntime().availableProcessors() * 4

    @EventListener(ApplicationReadyEvent::class)
    fun launchJpgDownloading() = runBlocking {
        imageDownloadingProcessorService.launchImageDownloading(coroutinesCount)
    }
}