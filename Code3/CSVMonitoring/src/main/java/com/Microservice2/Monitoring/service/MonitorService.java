package com.Microservice2.Monitoring.service;


import com.Microservice2.Monitoring.entity.Records;
import com.Microservice2.Monitoring.repo.Repo;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@Service
public class MonitorService {
    private final Repo repository;
    final Queue<Records> recordQueue = new LinkedList<>();
    private static final String CSV_FILE = "D:\\WaterQualityFile\\River_Water_Quality_Monitoring.csv";
    private long lastProcessedId = -1; // Stores the ID of the last processed record
    final Queue<Records> recordSender = new LinkedList<>();
    public MonitorService(Repo repository) {
        this.repository = repository;
    }

    /**
     * Runs when the application starts. Reads the CSV file but does NOT modify it.
     */
    @PostConstruct
    public void init() {
        System.out.println(" Server started - Processing existing CSV records...");
        readCsvAndQueueData(); // Load records into queue without modifying the file
        //startWatchingFile();   // Start monitoring for new records
    }
    private void readCsvAndQueueData() {
        try (CSVReader csvReader = new CSVReader(Files.newBufferedReader(Paths.get(CSV_FILE)))) {
            String[] values;
            csvReader.readNext(); // Skip header row

            while ((values = csvReader.readNext()) != null) {
                try {
                    long id = Long.parseLong(values[0]);

//                    // If this ID has already been processed, skip it
//                    if (id <= lastProcessedId) {
//                        continue;
//                    }

                    // Parse values, replacing missing ones with 0.0
                    double pHValue = (values[1] == null || values[1].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[1]);
                    double cuSol1Mgl = (values[6] == null || values[6].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[6]);
                    double cuSol2Ugl = (values[7] == null || values[7].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[7]);
                    double feSol1Ugl = (values[8] == null || values[8].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[8]);
                    double znSolUgl = (values[9] == null || values[9].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[9]);

                    // Create record object
                    Records record = new Records(id, LocalDateTime.now().toString(), pHValue, cuSol1Mgl, cuSol2Ugl, feSol1Ugl, znSolUgl);

                    // Add record to the queue
                    recordQueue.add(record);

                    // Update last processed ID
                   // lastProcessedId = id;

                } catch (NumberFormatException e) {
                    System.out.println("Skipping record due to invalid format: " + String.join(", ", values));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processes one record every 30 seconds.
     */
    @Scheduled(fixedRate = 30000) // Runs every 30 seconds
    public void processOneRecord() {
        if (!recordQueue.isEmpty()) {
            Records record = recordQueue.poll(); // Fetch one record
            recordSender.add(record);
            repository.save(record); // Save to database
            System.out.println("Processed record: " + record);
        } else {
            System.out.println("No new records to process.");
        }
    }

    /**
     * API to get the latest processed record.
     */
//    public Records getLatestRecord() {
//          if(recordSender.size() == 0)return null;
//        return recordSender.poll();
//    }

    @Transactional
    public Records getLatest() {
        Records rec = repository.findFirstByProcessedFalseOrderByTimestampAsc();

        if (rec != null) {
            repository.markAsProcessed(rec.getId());
        }

        return rec; // Return the updated record
    }

    public List<Records> getAllRecords() {
        return repository.findAll();
    }
}
