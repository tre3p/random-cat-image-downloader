package com.tre3p.randomizedjpgdownloader.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("summary")
class ImageStat(
    @Id val id: Long?,
    var filesCount: Int,
    var filesSize: Double,
    val statTimestamp: LocalDateTime
) {
    constructor(filesCount: Int, filesSize: Double, statTimestamp: LocalDateTime) : this(
        null,
        filesCount,
        filesSize,
        statTimestamp
    )
}