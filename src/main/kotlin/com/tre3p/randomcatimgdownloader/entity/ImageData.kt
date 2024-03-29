package com.tre3p.randomcatimgdownloader.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("files")
class ImageData(
    @Id val id: Long?,
    @Column(value = "url") var downloadUrl: String?,
    val size: Double,
    val contentType: String,
    val contentPath: String
) {
    constructor(size: Double, contentType: String, contentPath: String, downloadUrl: String) : this(
        null,
        downloadUrl,
        size,
        contentType,
        contentPath
    )
}
