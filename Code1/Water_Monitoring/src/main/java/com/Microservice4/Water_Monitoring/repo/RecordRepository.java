package com.Microservice4.Water_Monitoring.repo;

import com.Microservice4.Water_Monitoring.entity.CSVData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<CSVData,Long> {
    CSVData findFirstByProcessedFalseOrderByTimestampAsc();
}
