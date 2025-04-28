package com.microservice2.Quality.controller;

import com.microservice2.Quality.dto.MonitoringRecordDTO;
import com.microservice2.Quality.service.QualityService;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

//handle http requests
@RestController
@RequestMapping("/quality")
public class QualityController {
    private final QualityService qualityService;

    public QualityController(QualityService qualityService) {
        this.qualityService = qualityService;
    }
    //used to start the quality check
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/start-quality-check", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter startQualityCheck() {
        return qualityService.startContinuousQualityCheck();
    }
    @CrossOrigin(origins = "http://localhost:3000")
   //used to stop the quality check
    @GetMapping("/stop-quality-check")
    public String stopQualityCheck() {
        qualityService.stopQualityCheck();
        return "Water Quality Check Stopped.";
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("all")
    public MonitoringRecordDTO all(){
        return (MonitoringRecordDTO) qualityService.getAll();
    }
}
