package com.Quality.Water_Quality.repo;

import com.Quality.Water_Quality.entity.ProcessedRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedRecordRepository extends JpaRepository<ProcessedRecords,Long> {
}
