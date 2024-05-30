package com.tre3p.randomcatimgdownloader.service

import com.tre3p.randomcatimgdownloader.dto.ImageDto
import com.tre3p.randomcatimgdownloader.entity.ImageStat
import com.tre3p.randomcatimgdownloader.misc.AtomicDouble
import com.tre3p.randomcatimgdownloader.repository.ImageStatRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

@Service
class ImageStatService(private val imageStatRepository: ImageStatRepository) {

    lateinit var currentFilesCount: AtomicInteger

    lateinit var currentFilesSize: AtomicDouble

    private val log = KotlinLogging.logger {}

    @PostConstruct
    fun fetchCurrentStat() {
        log.info { "fetchCurrentStat(): fetching current stats.." }
        val currentFileStats = imageStatRepository.selectCurrentFileStats()
        log.info { "fetchCurrentStat(): current stats fetched. files count: ${currentFileStats.totalFilesCount}, files size: ${currentFileStats.totalFilesSize}" }

        currentFilesCount = AtomicInteger(currentFileStats.totalFilesCount)
        currentFilesSize = AtomicDouble(currentFileStats.totalFilesSize)
    }

    fun updateImageStat(imageDto: ImageDto) {
        log.debug { "+updateImageStat(): image size ${imageDto.imageSizeKb}, image download url: ${imageDto.downloadUrl}, image content type: ${imageDto.imageContentType}" }
        currentFilesSize.addAndGet(imageDto.imageSizeKb)
        currentFilesCount.incrementAndGet()

        imageStatRepository.save(ImageStat(currentFilesCount.get(), currentFilesSize.get(), LocalDateTime.now()))
        log.info { "updateImageStat(): image stats updated. current files count: ${currentFilesCount.get()}, current files size: ${currentFilesSize.get()}" }
        log.debug { "-updateImageStat()" }
    }

}