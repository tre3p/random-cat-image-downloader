package com.tre3p.randomizedjpgdownloader.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("files")
class ImageData(
    @Id val id: Long?,
    @Column(value = "url") var downloadUrl: String?,
    val size: Double,
    val contentType: String,
    val content: ByteArray
) {
    constructor(size: Double, contentType: String, content: ByteArray) : this(
        null,
        null,
        size,
        contentType,
        content
    )
}
