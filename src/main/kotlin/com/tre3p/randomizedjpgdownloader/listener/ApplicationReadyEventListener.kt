package com.tre3p.randomizedjpgdownloader.listener

import com.tre3p.randomizedjpgdownloader.service.RandomizedImageProcessorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(private val jpgDownloaderService: RandomizedImageProcessorService) {

    @EventListener(ApplicationReadyEvent::class)
    fun launchJpgDownloading() = runBlocking(Dispatchers.Default) {
        jpgDownloaderService.launchImagesProcessing()
    }
}