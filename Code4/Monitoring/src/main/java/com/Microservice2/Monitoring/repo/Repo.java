package com.Microservice2.Monitoring.repo;

import com.Microservice2.Monitoring.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo extends JpaRepository<Records, Long> {
}
