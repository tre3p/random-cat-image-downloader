package com.tre3p.randomizedjpgdownloader.repository

import com.tre3p.randomizedjpgdownloader.dto.ImageData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : CrudRepository<ImageData, Long>