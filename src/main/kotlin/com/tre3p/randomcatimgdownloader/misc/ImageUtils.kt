package com.tre3p.randomcatimgdownloader.misc

import java.util.UUID
import kotlin.random.Random

private const val IMAGE_DOWNLOAD_URL_TEMPLATE = "https://loremflickr.com/%s/%s"
private const val IMAGE_SIZE_LOWER_BOUND: Int = 10
private const val IMAGE_SIZE_UPPER_BOUND: Int = 5000

fun generateRandomImageDownloadUrl(): String {
    val width = Random.nextInt(IMAGE_SIZE_LOWER_BOUND, IMAGE_SIZE_UPPER_BOUND)
    val height = Random.nextInt(IMAGE_SIZE_LOWER_BOUND, IMAGE_SIZE_UPPER_BOUND)

    return String.format(IMAGE_DOWNLOAD_URL_TEMPLATE, width, height)
}

fun generateRandomImageFileName(): String = "${UUID.randomUUID()}.jpeg"