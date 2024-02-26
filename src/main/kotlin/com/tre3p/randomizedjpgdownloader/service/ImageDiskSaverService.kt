package com.tre3p.randomizedjpgdownloader.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.io.File

@Service
class ImageDiskSaverService {

    private val log = KotlinLogging.logger {}

    fun saveImageToDisk(path: String, content: ByteArray) {
        log.debug { "+saveImageToDisk(): path: $path, content length: ${content.size}" }
        val f = File(path)
        f.writeBytes(content)
        log.debug { "-saveImageToDisk()" }
    }
}