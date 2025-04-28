package com.microservice2.Quality.monitoringClient;

import com.microservice2.Quality.dto.MonitoringRecordDTO;
import org.apache.kafka.common.record.Records;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


//used to communicate to the monitoring microservice
@FeignClient(name = "monitoring")
public interface MonitoringClient {
    @GetMapping("/monitoring/get_record")
    public  MonitoringRecordDTO getNextMonitoringRecord();
    @GetMapping("/monitoring/all")
    public  List<MonitoringRecordDTO> getAllRecords();
}
