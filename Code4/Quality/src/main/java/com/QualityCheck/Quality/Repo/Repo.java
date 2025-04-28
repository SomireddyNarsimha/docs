package com.QualityCheck.Quality.Repo;

import com.QualityCheck.Quality.Dto.Records;
import com.QualityCheck.Quality.entity.QualityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo extends JpaRepository<QualityRecord, Long> {
}
