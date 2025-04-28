package com.microservice2.Quality.service;

import com.microservice2.Quality.dto.MonitoringRecordDTO;
import com.microservice2.Quality.entity.QualityData;
import com.microservice2.Quality.monitoringClient.MonitoringClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice2.Quality.repo.QualityRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class QualityService {
    private final MonitoringClient monitoringClient;
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean running = false;
    private Thread backgroundThread;
    private QualityRepo qualityRepo;
    public QualityService(MonitoringClient monitoringClient,QualityRepo qp) {
        this.monitoringClient = monitoringClient;
        this.qualityRepo  = qp;
    }

    public SseEmitter startContinuousQualityCheck() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        if (!running) {  // Ensure only one background task runs at a time
            running = true;
            startBackgroundTask();
        }

        return emitter;
    }
    //used to retrive the data from the other Monitoring microservice
    private void startBackgroundTask() {
        backgroundThread = new Thread(() -> {
            while (running) {
                MonitoringRecordDTO record = monitoringClient.getNextMonitoringRecord();
                if (record != null && !qualityRepo.existsById(record.getId())) {

                    sendDataToClients(record);
                }

                try {
                    Thread.sleep(30000); // Fetch new records every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        backgroundThread.start();
    }
    //checks the quality,stores the status data in the table and send the data to client
    private void sendDataToClients(MonitoringRecordDTO record) {
        String qualityStatus = (record.getPh() >= 6.5 && record.getPh() <= 8.5) ? "Green-Flag" : "Red-Flag";
        MonitoringRecordDTO filteredRecord = new MonitoringRecordDTO(record.getId(), record.getPh(), qualityStatus,record.getUuid());
        QualityData qd =  new QualityData(record.getId(),record.getPh(),qualityStatus,record.getUuid());
        System.out.println(qd.toString());
        qualityRepo.save(qd);

        try {
            String jsonData = objectMapper.writeValueAsString(filteredRecord);
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().data(jsonData));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //used to stop the quality check
    public void stopQualityCheck() {
        running = false;
        if (backgroundThread != null) {
            backgroundThread.interrupt();
        }
        emitters.forEach(SseEmitter::complete);
        emitters.clear();
    }

    public MonitoringRecordDTO getAll() {
        List<MonitoringRecordDTO> dto = monitoringClient.getAllRecords();
        double ph = 0;
        double cusol1MgL = 0;
         double cusol2UgL = 0;
        double fesol1UgL = 0;
        double znSolUgL = 0;

        for(MonitoringRecordDTO record : dto){
            ph+=record.getPh();
            cusol1MgL  = record.getCusol1MgL();
            cusol2UgL = record.getCusol2UgL();
            fesol1UgL = record.getFesol1UgL();
            znSolUgL = record.getZnSolUgL();

        }
        MonitoringRecordDTO  ans = new MonitoringRecordDTO();
        int size = dto.size();
        ans.setPh(ph/dto.size());
        ans.setCusol1MgL(cusol1MgL/size);
        ans.setCusol2UgL(cusol2UgL/size);
        ans.setFesol1UgL(fesol1UgL/size);
        ans.setZnSolUgL(znSolUgL/size);
        return ans;

    }
}
