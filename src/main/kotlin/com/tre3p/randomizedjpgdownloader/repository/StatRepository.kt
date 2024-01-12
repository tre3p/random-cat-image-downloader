package com.tre3p.randomizedjpgdownloader.repository

import com.tre3p.randomizedjpgdownloader.dto.CurrentImageStat
import com.tre3p.randomizedjpgdownloader.entity.ImageStat
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StatRepository : CrudRepository<ImageStat, Long> {

    @Query("select count(*) as total_files_count, sum(size) as total_files_size from files")
    fun selectCurrentFileStats(): CurrentImageStat

}