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
        log.info { "+fetchCurrentStat(): fetching current stats.." }
        val currentFileStats = imageStatRepository.selectCurrentFileStats()
        log.info { "fetchCurrentStat(): current stats fetched. files count: ${currentFileStats.totalCount}, files size: ${currentFileStats.totalSize}" }

        currentFilesCount = AtomicInteger(currentFileStats.totalCount)
        currentFilesSize = AtomicLong(currentFileStats.totalSize.toRawBits())
    }

    fun updateImageStat(imageData: ImageData) {
        log.info { "+updateImageStat(): image size: ${imageData.size}" }
        currentFilesSize.addDoubleBits(imageData.size)
        currentFilesCount.incrementAndGet()

        imageStatRepository.save(ImageStat(currentFilesCount.get(), Double.fromBits(currentFilesSize.get()), LocalDateTime.now()))
        log.info { "-updateImageStat(): image stats updated" }
    }

    private fun AtomicLong.addDoubleBits(double: Double) = this.updateAndGet { prevVal -> (Double.fromBits(prevVal) + double).toRawBits() }
}