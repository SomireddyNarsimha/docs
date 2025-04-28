package com.Microservice2.Monitoring.controller;

import com.Microservice2.Monitoring.entity.Records;
import com.Microservice2.Monitoring.service.MonitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonitoringControllerTest {

    @Mock
    private MonitorService monitorService;

    @InjectMocks
    private MonitoringController monitoringController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test when a latest record exists, it should return HTTP 200 OK.
     */
    @Test
    void testGetLatestRecord_Success() {
        // Mock a sample record
        Records mockRecord = new Records(1L, LocalDateTime.now(), 7.0, 0.5, 10.0, 20.0, 30.0);

        // Simulate service returning a record
        when(monitorService.getLatestRecord()).thenReturn(mockRecord);

        // Call the controller method
        ResponseEntity<Records> response = monitoringController.getLatestRecord();

        // Verify response
        assertEquals(200, response.getStatusCodeValue()); // HTTP 200
        assertNotNull(response.getBody());
        assertEquals(mockRecord, response.getBody());

        // Ensure the service method was called once
        verify(monitorService, times(1)).getLatestRecord();
    }

    /**
     * Test when no latest record is found, it should return HTTP 404 Not Found.
     */
    @Test
    void testGetLatestRecord_NotFound() {
        // Simulate service returning null
        when(monitorService.getLatestRecord()).thenReturn(null);

        // Call the controller method
        ResponseEntity<Records> response = monitoringController.getLatestRecord();

        // Verify response
        assertEquals(404, response.getStatusCodeValue()); // HTTP 404
        assertNull(response.getBody());

        // Ensure the service method was called once
        verify(monitorService, times(1)).getLatestRecord();
    }
}