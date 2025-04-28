package com.microservice1.monitoring.Controller;

import com.microservice1.monitoring.Service.MonitorService;
import com.microservice1.monitoring.entity.Records;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {
    private final MonitorService monitoringService;

    public MonitoringController(MonitorService monitoringService) {
        this.monitoringService = monitoringService;
    }
    //fetching record
    @GetMapping("/get_record")
    public Records getNextMonitoringRecord() {
      //  System.out.println("getNextMonitoringRecord called");

         Records record = null;
        try {
            record = monitoringService.getLatestRecord();
            if(record == null){
                System.out.println("No record found");
            }else{
                System.out.println("Record found"+record);
            }
        } catch (Exception e) {
            System.err.println("Error while fetching record: " + e.getMessage());
            e.printStackTrace(); // This will show the full stack trace
        }

        return record;
    }
    @GetMapping("/all")
    public List<Records> getAll(){
        return monitoringService.getAllRecords();
    }

}