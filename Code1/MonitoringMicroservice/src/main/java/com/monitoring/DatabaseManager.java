package com.monitoring;

import com.Common.QualityStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_PATH = "MonitoringMicroservice/monitoring.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS records (id INTEGER PRIMARY KEY, uuid TEXT, timestamp TEXT, pH REAL, cuSol1 REAL, cuSol2 REAL, feSol1 REAL, znSol REAL, processed INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void storeData(Record record) {
        String sql = "INSERT OR IGNORE INTO records (uuid, timestamp, pH, cuSol1, cuSol2, feSol1, znSol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, record.getUuid());
            pstmt.setString(2, record.getTimestamp().toString());
            pstmt.setDouble(3, record.getph());
            pstmt.setDouble(4, record.getcusol1MgL());
            pstmt.setDouble(5, record.getcusol2UgL());
            pstmt.setDouble(6, record.getfesol1UgL());
            pstmt.setDouble(7, record.getZnSolUgL());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Record getLatestRecord() {
        String sql = "SELECT * FROM records ORDER BY timestamp DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Record(
                        rs.getLong("id"),
                        rs.getString("uuid"),
                        rs.getString("timestamp"),
                        rs.getDouble("pH"),
                        rs.getDouble("cuSol1"),
                        rs.getDouble("cuSol2"),
                        rs.getDouble("feSol1"),
                        rs.getDouble("znSol")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Record> getAllRecords() {
        List<Record> recordList = new ArrayList<>();
        String sql = "SELECT id, uuid, timestamp, pH, cuSol1, cuSol2, feSol1, znSol FROM records";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Record record = new Record(
                        rs.getLong("id"),
                        rs.getString("uuid"),
                        rs.getString("timestamp"),
                        rs.getDouble("pH"),
                        rs.getDouble("cuSol1"),
                        rs.getDouble("cuSol2"),
                        rs.getDouble("feSol1"),
                        rs.getDouble("znSol")
                );
                recordList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordList;
    }

}
