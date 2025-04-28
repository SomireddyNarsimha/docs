package com.Common;

import okhttp3.*;

public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient();

    public static String post(String url,String bo) throws Exception {
        RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("HTTP error code: " + response.code());
            }
            return response.body().string();
        }
    }

    public static String get(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("HTTP error code: " + response.code());
            }
            return response.body().string();
        }
    }
    // ✅ Method to send quality status to the client
    public static void sendStatusToClient(QualityStatus status) throws Exception {
        // Convert QualityStatus object to JSON using simple string formatting
        String jsonPayload = String.format(
                "{\"uuid\":\"%s\",\"ph\":%.2f,\"qualityStatus\":\"%s\"}",
                status.getUuid(), status.getpH(), status.getStatus()
        );

        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        // Assuming the client is listening on a specific endpoint
        Request request = new Request.Builder()
                .url("http://localhost:8080/quality/status") // Adjust as per your client-side API
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to send status to client: " + response.code());
            }
            System.out.println("Status sent to client: " + status);
        }
    }
}
