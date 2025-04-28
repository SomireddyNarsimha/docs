package com.microservice1.monitoring.Repository;

import com.microservice1.monitoring.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
//used to do operations on the database
@Repository
public interface Repo extends JpaRepository<Records, Long> {
}
