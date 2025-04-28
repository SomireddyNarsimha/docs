package com.QualityCheck.Quality.Controller;

import com.QualityCheck.Quality.Dto.Records;
import com.QualityCheck.Quality.Service.QualityService;
import com.QualityCheck.Quality.entity.QualityRecord;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/quality")
public class QualityController {
    private final QualityService qualityService;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private SseEmitter emitter;

    public QualityController(QualityService qualityService) {
        this.qualityService = qualityService;
    }

    /**
     * Starts streaming real-time water quality updates to the client.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamQualityCheck() {
        System.out.println("The quality check started");
        stopQualityCheck(); // Ensure no previous emitter is running
        emitter = new SseEmitter(Long.MAX_VALUE);
        isRunning.set(true);

        emitter.onCompletion(this::stopQualityCheck);
        emitter.onTimeout(this::stopQualityCheck);
        emitter.onError((e) -> stopQualityCheck());

        new Thread(this::runQualityCheck).start(); // Run in a separate thread
        return emitter;
    }

    /**
     * Stops the quality check process.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/stop")
    public ResponseEntity<String> stopQualityCheck() {
        System.out.println("Quality check stopped.");
        isRunning.set(false);
        if (emitter != null) {
            emitter.complete();
            emitter = null;
        }
        return ResponseEntity.ok("Quality check stopped.");
    }

    /**
     * Fetches water quality data and streams it to the client in JSON format.
     */
    private void runQualityCheck() {
        while (isRunning.get()) {
            try {
                Records latestRecord = qualityService.fetchLatestRecord();

                if (latestRecord != null) {
                    QualityRecord isSafe = qualityService.checkWaterQuality(latestRecord);

                    System.out.println("Sending JSON Data: " + isSafe); // Log to terminal

                    if (emitter != null) {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data(isSafe, MediaType.APPLICATION_JSON)); // Send JSON object
                        } catch (IOException e) {
                            System.out.println("Client disconnected: " + e.getMessage());
                            stopQualityCheck();
                        }
                    }
                }

                Thread.sleep(30000); // Wait 30 seconds before fetching the next record
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Quality check stopped.");
                break;
            }
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/all")
    public Records getRecord(){
        return qualityService.getRecords();
    }

}
