package com.quality;
import com.Common.QualityStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QualityDatabaseManager {
    private static  String MONITOR_DB_PATH = "MonitoringMicroservice/monitoring.db";
    private static  String QUALITY_DB_PATH = "QualityMicroservice/quality.db";
    // New method to set database paths for testing
    public static void setDatabasePaths(String monitorDbPath, String qualityDbPath) {
        MONITOR_DB_PATH = monitorDbPath;
        QUALITY_DB_PATH = qualityDbPath;
    }


    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + QUALITY_DB_PATH);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS quality_status (id INTEGER PRIMARY KEY, uuid TEXT, pH REAL, qualityStatus TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static QualityStatus processNextRecord() {
        String selectSQL = "SELECT * FROM records WHERE processed = 0 ORDER BY timestamp ASC LIMIT 1";
        String updateSQL = "UPDATE records SET processed = 1 WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + MONITOR_DB_PATH);
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
             PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
             ResultSet rs = selectStmt.executeQuery()) {

            if (rs.next()) {
                double pH = rs.getDouble("pH");
                String status = (pH >= 6.5 && pH <= 8.5) ? "Green" : "Red";

                updateStmt.setLong(1, rs.getLong("id"));
                updateStmt.executeUpdate();

                return new QualityStatus(rs.getString("uuid"), pH, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void storeQualityStatus(QualityStatus status) {
        String sql = "INSERT INTO quality_status (uuid, pH, qualityStatus) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + QUALITY_DB_PATH);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.getUuid());
            pstmt.setDouble(2, status.getpH());
            pstmt.setString(3, status.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<QualityStatus> getAllQualityStatus() {
        List<QualityStatus> statusList = new ArrayList<>();
        String sql = "SELECT * FROM quality_status ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + QUALITY_DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                QualityStatus status = new QualityStatus(
                        rs.getString("uuid"),
                        rs.getDouble("pH"),
                        rs.getString("qualityStatus")
                );
                statusList.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusList;
    }
}
