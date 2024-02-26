package com.tre3p.randomcatimgdownloader.dto

data class ImageDto(
    val imageBytes: ByteArray,
    val downloadUrl: String,
    val imageContentType: String,
    val imageSizeKb: Double
)
