package com.tre3p.randomcatimgdownloader.repository

import com.tre3p.randomcatimgdownloader.dto.CurrentImageStat
import com.tre3p.randomcatimgdownloader.entity.ImageStat
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageStatRepository : CrudRepository<ImageStat, Long> {

    @Query("select count(*) as total_files_count, sum(size) as total_files_size from files")
    fun selectCurrentFileStats(): CurrentImageStat
}