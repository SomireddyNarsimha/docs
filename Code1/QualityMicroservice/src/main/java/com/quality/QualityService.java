package com.quality;
import static spark.Spark.*;
import com.Common.HttpClient;
import com.Common.QualityStatus;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class QualityService {

    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        port(8097);
        QualityDatabaseManager.initializeDatabase();

        post("quality/start", (req, res) -> {
            isRunning.set(true);
            new Thread(QualityService::runQualityCheck).start();
            return "Quality check started";
        });

        post("quality/stop", (req, res) -> {
            isRunning.set(false);
            return "Quality check stopped";
        });
        // Get all quality status records in descending order

        get("/quality/records", (req, res) -> {
            res.type("application/json");
            List<QualityStatus> statusList = QualityDatabaseManager.getAllQualityStatus();
            return objectMapper.writeValueAsString(statusList);
        });



        // get("quality/status", (req, res) -> QualityDatabaseManager.getLatestStatus());
    }

    static void runQualityCheck() {
        while (isRunning.get()) {
            try {
                QualityStatus status = QualityDatabaseManager.processNextRecord();
                if (status != null) {
                    QualityDatabaseManager.storeQualityStatus(status);
                   // HttpClient.sendStatusToClient(status);
                }
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
