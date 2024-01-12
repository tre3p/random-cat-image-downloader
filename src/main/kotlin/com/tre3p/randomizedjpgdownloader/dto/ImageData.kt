package com.tre3p.randomizedjpgdownloader.dto

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("files")
class ImageData(
    @Id val id: Long?,
    @Column(value = "url") var downloadUrl: String?,
    val size: Long,
    val contentType: String,
    val content: ByteArray
) {
    constructor(size: Long, contentType: String, content: ByteArray): this(
        null,
        null,
        size,
        contentType,
        content
    )
}
