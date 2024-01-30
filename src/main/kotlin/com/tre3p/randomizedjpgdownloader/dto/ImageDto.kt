package com.tre3p.randomizedjpgdownloader.dto

data class ImageDto(
    val imageBytes: ByteArray,
    val downloadUrl: String,
    val imageContentType: String,
    val imageSizeKb: Double
)
