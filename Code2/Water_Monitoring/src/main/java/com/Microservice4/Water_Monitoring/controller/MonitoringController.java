package com.Microservice4.Water_Monitoring.controller;


import com.Microservice4.Water_Monitoring.entity.CSVData;
import com.Microservice4.Water_Monitoring.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MonitoringController {
    @Autowired
        CSVService service;
    @GetMapping("/total")
    public ResponseEntity<List<CSVData>> fetchAll() {
        List<CSVData> data = service.findAll();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
