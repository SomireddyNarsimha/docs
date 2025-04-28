package com.microservice2.Quality.repo;

import com.microservice2.Quality.entity.QualityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//use to do operation on the Quality table
@Repository
public interface QualityRepo extends JpaRepository<QualityData,Long> {
}
