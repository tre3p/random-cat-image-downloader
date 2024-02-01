package com.tre3p.randomizedjpgdownloader.service

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import com.tre3p.randomizedjpgdownloader.entity.ImageStat
import com.tre3p.randomizedjpgdownloader.repository.ImageStatRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

@Service
class ImageStatService(private val imageStatRepository: ImageStatRepository) {

    private lateinit var currentFilesCount: AtomicInteger

    private lateinit var currentFilesSize: AtomicLong

    private val log = KotlinLogging.logger {}

    @PostConstruct
    fun fetchCurrentStat() {
        log.info { "fetchCurrentStat(): fetching current stats.." }
        val currentFileStats = imageStatRepository.selectCurrentFileStats()
        log.info { "fetchCurrentStat(): current stats fetched. files count: ${currentFileStats.totalFilesCount}, files size: ${currentFileStats.totalFilesSize}" }

        currentFilesCount = AtomicInteger(currentFileStats.totalFilesCount)
        currentFilesSize = AtomicLong(currentFileStats.totalFilesSize.toRawBits())
    }

    fun updateImageStat(imageData: ImageData) {
        log.debug { "+updateImageStat(): image size ${imageData.size}, image download url: ${imageData.downloadUrl}, image content type: ${imageData.contentType}" }
        currentFilesSize.addDoubleBits(imageData.size)
        currentFilesCount.incrementAndGet()

        imageStatRepository.save(ImageStat(currentFilesCount.get(), Double.fromBits(currentFilesSize.get()), LocalDateTime.now()))
        log.info { "updateImageStat(): image stats updated. current files count: ${currentFilesCount.get()}, current files size: ${Double.fromBits(currentFilesSize.get())}" }
        log.debug { "-updateImageStat()" }
    }

    private fun AtomicLong.addDoubleBits(double: Double) = this.updateAndGet { prevVal -> (Double.fromBits(prevVal) + double).toRawBits() }
}