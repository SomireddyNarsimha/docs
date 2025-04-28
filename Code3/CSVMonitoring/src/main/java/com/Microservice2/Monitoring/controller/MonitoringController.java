package com.Microservice2.Monitoring.controller;


import com.Microservice2.Monitoring.entity.Records;
import com.Microservice2.Monitoring.service.MonitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monitor")
public class MonitoringController {
    private final MonitorService monitorService;

    public MonitoringController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    /**
     * Fetch the latest processed record from the database.
     */
    @GetMapping("/latest")
    public ResponseEntity<Records> getLatest() {
        Records latestRecord = monitorService.getLatest();
        if (latestRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestRecord);
    }
    @GetMapping("all")
    public ResponseEntity<List<Records>> getAll(){
        List<Records> record = monitorService.getAllRecords();
        if(record == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

}