package com.gateway;

import com.Common.HttpClient;

import java.io.IOException;
import java.io.PrintWriter;

import static spark.Spark.*;

public class ApiGateway {
    public static void main(String[] args) {
        port(8090); // Running API Gateway on port 8090

        enableCORS(); // Enable CORS for all requests

        // Monitoring Microservice Endpoints
        post("/start", (req, res) -> {
            res.type("application/json");
            return HttpClient.post("http://localhost:8098/start", req.body());
        });

        // Quality Microservice Endpoints
        post("/quality/start", (req, res) -> {
            res.type("application/json");
            return HttpClient.post("http://localhost:8097/quality/start", req.body());
        });

        post("/quality/stop", (req, res) -> {
            res.type("application/json");
            return HttpClient.post("http://localhost:8097/quality/stop", req.body());
        });

        // Get all quality records (Descending Order)
        // ✅ SSE Endpoint for real-time quality records
        get("/quality/records", (req, res) -> {
           res.type("application/json");
           return HttpClient.get("http://localhost:8097/quality/records");
        });

        // Get total records
        get("/totalrecords", (req, res) -> {
            res.type("application/json");
            return HttpClient.get("http://localhost:8098/allrecords");
        });
    }

    // ✅ Function to enable CORS
    private static void enableCORS() {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*"); // Allow all origins
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.header("Access-Control-Allow-Credentials", "true"); // Allow credentials
        });

        options("/*", (request, response) -> {
            response.status(200);
            return "OK";
        });
    }
}
