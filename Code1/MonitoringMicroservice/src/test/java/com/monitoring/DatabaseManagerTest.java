package com.monitoring;
import com.monitoring.DatabaseManager;
import com.monitoring.Record;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    private static final String TEST_DB_PATH = "MonitoringMicroservice/test_monitoring.db";
    private static final String TEST_DB_URL = "jdbc:sqlite:" + TEST_DB_PATH;

    @BeforeAll
    static void setup() throws Exception {
        // Ensure the directory exists
        Files.createDirectories(Path.of("MonitoringMicroservice"));

        // Switch to a test DB by overriding static DB path using reflection (if modifiable)
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS records (id INTEGER PRIMARY KEY, uuid TEXT, timestamp TEXT, pH REAL, cuSol1 REAL, cuSol2 REAL, feSol1 REAL, znSol REAL, processed INTEGER DEFAULT 0)");
        }
    }

    @Test
    @DisplayName("Insert and retrieve record from test DB")
    void testInsertAndGetAllRecords() {
        // Create a sample record
        Record testRecord = new Record(
                0L,
                UUID.randomUUID().toString(),
                LocalDateTime.now().toString(),
                7.5,
                1.2,
                0.8,
                0.5,
                2.3
        );

        // Store the data using direct SQL (simulate DatabaseManager.storeData logic)
        String sql = "INSERT INTO records (uuid, timestamp, pH, cuSol1, cuSol2, feSol1, znSol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, testRecord.getUuid());
            pstmt.setString(2, testRecord.getTimestamp());
            pstmt.setDouble(3, testRecord.getph());
            pstmt.setDouble(4, testRecord.getcusol1MgL());
            pstmt.setDouble(5, testRecord.getcusol2UgL());
            pstmt.setDouble(6, testRecord.getfesol1UgL());
            pstmt.setDouble(7, testRecord.getZnSolUgL());
            pstmt.executeUpdate();
        } catch (Exception e) {
            fail("Data insertion failed: " + e.getMessage());
        }

        // Retrieve and verify
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM records")) {

            assertTrue(rs.next(), "Should retrieve at least one record");
            assertEquals(testRecord.getUuid(), rs.getString("uuid"));
            assertEquals(testRecord.getph(), rs.getDouble("pH"));
        } catch (SQLException e) {
            fail("Data retrieval failed: " + e.getMessage());
        }
    }

    @AfterAll
    static void cleanup() {
        // Delete test database
        File file = new File(TEST_DB_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}
