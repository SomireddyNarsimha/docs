package com.Microservice4.Water_Monitoring.service;

import com.Microservice4.Water_Monitoring.entity.CSVData;
import com.Microservice4.Water_Monitoring.repo.RecordRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CSVService {
    private final RecordRepository repository;
    private static final String CSV_FILE = "D:\\WaterQualityFile\\River_Water_Quality_Monitoring.csv";
    private static final String DB_URL = "jdbc:sqlite:D:\\Project4\\recordStorage.db";

    public CSVService(RecordRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            enableWALMode(connection);
            readCsvAndQueueData(connection);
            System.out.println("Initialization successful");
        } catch (Exception e) {
            System.err.println("Initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error during CSVService initialization", e);
        }
    }

    public void readCsvAndQueueData(Connection connection) {
        try (CSVReader csvReader = new CSVReader(Files.newBufferedReader(Paths.get(CSV_FILE)))) {
            String[] values;
            csvReader.readNext(); // Skip header row

            while ((values = csvReader.readNext()) != null) {
                try {
                    long id = Long.parseLong(values[0]);

                    // Parse values, replacing missing ones with 0.0
                    double pHValue = (values[1] == null || values[1].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[1]);
                    double cuSol1Mgl = (values[6] == null || values[6].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[6]);
                    double cuSol2Ugl = (values[7] == null || values[7].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[7]);
                    double feSol1Ugl = (values[8] == null || values[8].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[8]);
                    double znSolUgl = (values[9] == null || values[9].trim().isEmpty()) ? 0.0 : Double.parseDouble(values[9]);

                    CSVData record = new CSVData(id, LocalDateTime.now().toString(), pHValue, cuSol1Mgl, cuSol2Ugl, feSol1Ugl, znSolUgl);

                    if (repository.existsById(id)) {
                        System.out.println("Id already exists");
                    } else {
                        repository.save(record);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping record due to invalid format: " + String.join(", ", values));
                }
            }

            System.out.println("All records inserted successfully.");
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void enableWALMode(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute("PRAGMA synchronous=NORMAL;");
            System.out.println("WAL mode enabled for SQLite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CSVData> findAll() {
        return repository.findAll();
    }
}
