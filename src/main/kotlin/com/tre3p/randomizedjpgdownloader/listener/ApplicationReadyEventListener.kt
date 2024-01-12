package com.tre3p.randomizedjpgdownloader.listener

import com.tre3p.randomizedjpgdownloader.service.RandomizedJpgDownloaderService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(private val jpgDownloaderService: RandomizedJpgDownloaderService) {

    @EventListener(ApplicationReadyEvent::class)
    suspend fun launchJpgDownloading() {
        jpgDownloaderService.launchJpegsDownloading()
    }
}