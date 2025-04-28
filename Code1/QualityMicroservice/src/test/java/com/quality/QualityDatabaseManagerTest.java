package com.quality;

import com.quality.QualityDatabaseManager;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.Common.QualityStatus;
import java.sql.*;
import java.util.List;

public class QualityDatabaseManagerTest {

    // Setup for the test - Initialize the database
    @BeforeAll
    static void setup() {
        // Set database paths for testing (use test dbs to avoid affecting production data)
        QualityDatabaseManager.setDatabasePaths("test_monitoring.db", "test_quality.db");
        QualityDatabaseManager.initializeDatabase();
    }

    // Test for initializing the database
    @Test
    void testInitializeDatabase() {
        // Check if the quality_status table exists (there is no direct query for this, so we check after a call)
        assertDoesNotThrow(() -> {
            // Insert a mock record
            QualityStatus status = new QualityStatus("1234", 7.5, "Green");
            QualityDatabaseManager.storeQualityStatus(status);

            // Check if it gets stored correctly
            List<QualityStatus> statusList = QualityDatabaseManager.getAllQualityStatus();
            assertTrue(statusList.size() > 0);
        });
    }

//    // Test the method to process the next record
//    @Test
//    void testProcessNextRecord() {
//        // Assuming there is a record in the database with processed = 0
//        QualityStatus status = QualityDatabaseManager.processNextRecord();
//        assertNotNull(status, "The processed record should not be null");
//
//        // Verify the quality status and pH value
//        assertTrue(status.getpH() >= 0, "pH value should be valid");
//        assertTrue(status.getStatus().equals("Green") || status.getStatus().equals("Red"),
//                "Status should be either Green or Red");
//    }

    // Test storing a quality status
    @Test
    void testStoreQualityStatus() {
        QualityStatus status = new QualityStatus("5678", 6.9, "Green");
        assertDoesNotThrow(() -> {
            QualityDatabaseManager.storeQualityStatus(status);
            List<QualityStatus> statusList = QualityDatabaseManager.getAllQualityStatus();
            assertTrue(statusList.stream().anyMatch(s -> s.getUuid().equals("5678")),
                    "The stored status should exist in the database.");
        });
    }

    // Test retrieving all quality statuses
    @Test
    void testGetAllQualityStatus() {
        List<QualityStatus> statusList = QualityDatabaseManager.getAllQualityStatus();
        assertTrue(statusList.size() > 0, "There should be at least one quality status in the database.");
    }

    @AfterAll
    static void cleanup() {
        // Cleanup or reset databases after tests (Optional, can be handled manually)
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test_quality.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS quality_status");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
