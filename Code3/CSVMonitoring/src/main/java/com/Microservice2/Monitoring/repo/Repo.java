package com.Microservice2.Monitoring.repo;

import com.Microservice2.Monitoring.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Repo extends JpaRepository<Records, Long> {
    Records findFirstByProcessedFalseOrderByTimestampAsc();
    @Modifying
    @Transactional
    @Query("UPDATE Records r SET r.processed = true WHERE r.id = :id")
    void markAsProcessed(@Param("id") Long id);
}
