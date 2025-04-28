package com.Quality.Water_Quality.service;

import com.Quality.Water_Quality.Dto.Records;
import com.Quality.Water_Quality.entity.ProcessedRecords;
import com.Quality.Water_Quality.repo.ProcessedRecordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.UUID;

@Service
public class QualityService {

    private final ProcessedRecordRepository repo;
    private static final String DB_URL = "jdbc:sqlite:recordStorage.db?busy_timeout=5000";

    public QualityService(ProcessedRecordRepository repo) {
        this.repo = repo;
    }

    public Records fetchLatestRecord() {
        String query = "SELECT * FROM storage WHERE processed = false ORDER BY timestamp ASC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Records record = extractRecordFromResultSet(rs);
                markRecordAsProcessed(record.getId());
                return record;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Records extractRecordFromResultSet(ResultSet rs) throws SQLException {
        return new Records(
                rs.getLong("record_id"),
                rs.getString("timestamp"),
                rs.getDouble("ph"),
                rs.getDouble("cu_sol1mgl"),
                rs.getDouble("cu_sol2ugl"),
                rs.getDouble("fe_sol1ugl"),
                rs.getDouble("zn_sol_ugl")
        );
    }

    public ProcessedRecords checkWaterQuality(Records record) {
        if (record == null) return null;

        String uniqueUuid = (record.getUuid() != null) ? record.getUuid() : UUID.randomUUID().toString();
        double tds = calculateTDS(record);
        boolean isQualityGood = isWaterQualityGood(record.getph(), tds);

        ProcessedRecords qd = new ProcessedRecords(
                record.getId(),
                record.getph(),
                isQualityGood ? "Green" : "Red",
                uniqueUuid
        );
        repo.save(qd);
        return qd;
    }

    private double calculateTDS(Records record) {
        return record.getCusol1MgL() +
                (record.getCusol2UgL() * 0.001) +
                (record.getFesol1UgL() * 0.001) +
                (record.getZnSolUgL() * 0.001);
    }

    private boolean isWaterQualityGood(double pH, double tds) {
        return pH >= 6.5 && pH <= 8.5 && tds <= 500;
    }

    public void markRecordAsProcessed(Long recordId) {
        String updateQuery = "UPDATE storage SET processed = true WHERE record_id = ?";
        final int MAX_RETRIES = 5;
        int retryCount = 0;

        while (retryCount < MAX_RETRIES) {
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

                stmt.setLong(1, recordId);
                int updatedRows = stmt.executeUpdate();

                if (updatedRows > 0) {
                    System.out.println("Successfully marked record " + recordId + " as processed");
                    return;
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 5) { // SQLITE_BUSY error code
                    retryCount++;
                    System.out.println("Database busy, retry attempt " + retryCount);
                    try {
                        Thread.sleep(100); // Exponential backoff could be added here
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    e.printStackTrace();
                    break;
                }
            }
        }
        System.err.println("Failed to mark record " + recordId + " as processed after " + MAX_RETRIES + " attempts");
    }

    @PostConstruct
    public void enableWALMode() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {

            // Enable Write-Ahead Logging (WAL) mode
            stmt.execute("PRAGMA journal_mode=WAL;");
            // Set synchronous mode to NORMAL for better performance
            stmt.execute("PRAGMA synchronous=NORMAL;");
            // Increase the busy timeout
            stmt.execute("PRAGMA busy_timeout=5000;");

            System.out.println("Database WAL mode configured successfully");
        } catch (SQLException e) {
            System.err.println("Error configuring database WAL mode:");
            e.printStackTrace();
        }
    }



    public Records getAllRecords() {
        String query = "SELECT * FROM storage";
        double ph = 0;
        double cusol1MgL = 0;
        double cusol2UgL = 0;
        double fesol1UgL = 0;
        double znSolUgL = 0;
        int count = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Records record = extractRecordFromResultSet(rs);
                ph+=record.getph();
                cusol1MgL+= record.getCusol1MgL();
                cusol2UgL+= record.getCusol2UgL();
                fesol1UgL+=record.getFesol1UgL();
                znSolUgL+= record.getZnSolUgL();
               count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count == 0) return null;  // Avoid division by zero

        Records ans = new Records();
        ans.setph(ph / count);
        ans.setCusol1MgL(cusol1MgL / count);
        ans.setCusol2UgL(cusol2UgL / count);
        ans.setFesol1UgL(fesol1UgL / count);
        ans.setZnSolUgL(znSolUgL / count);

        return ans;
    }

}