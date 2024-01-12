package com.tre3p.randomizedjpgdownloader.listener

import com.tre3p.randomizedjpgdownloader.service.RandomizedImageDownloaderService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(private val jpgDownloaderService: RandomizedImageDownloaderService) {

    @EventListener(ApplicationReadyEvent::class)
    fun launchJpgDownloading() = runBlocking {
        jpgDownloaderService.launchImagesDownloading()
    }
}