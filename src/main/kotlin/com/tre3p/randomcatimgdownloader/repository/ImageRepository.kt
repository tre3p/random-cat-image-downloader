package com.tre3p.randomcatimgdownloader.repository

import com.tre3p.randomcatimgdownloader.entity.ImageData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : CrudRepository<ImageData, Long>