package com.tre3p.randomizedjpgdownloader.repository

import com.tre3p.randomizedjpgdownloader.entity.ImageData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : CrudRepository<ImageData, Long>