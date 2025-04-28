package com.QualityCheck.Quality.Service;

import com.QualityCheck.Quality.Dto.Records;
import com.QualityCheck.Quality.Repo.Repo;
import com.QualityCheck.Quality.entity.QualityRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QualityServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Repo repo;

    @InjectMocks
    private QualityService qualityService;

    private static final String MONITOR_SERVICE_URL = "http://localhost:8084/monitor/latest";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test fetching the latest record from Monitoring Service.
     */
    @Test
    void testFetchLatestRecord() {
        // Mock response from monitoring service
        Records mockRecord = new Records(1L, LocalDateTime.now(), 7.2, 0.3, 200.0, 150.0, 100.0);

        when(restTemplate.getForObject(MONITOR_SERVICE_URL, Records.class)).thenReturn(mockRecord);

        Records fetchedRecord = qualityService.fetchLatestRecord();

        assertNotNull(fetchedRecord);
        assertEquals(mockRecord, fetchedRecord);

        verify(restTemplate, times(1)).getForObject(MONITOR_SERVICE_URL, Records.class);
    }

    /**
     * Test checking water quality when the record is within safe limits.
     */
    @Test
    void testCheckWaterQuality_Safe() {
        // Mock record with safe pH and TDS
        Records safeRecord = new Records(2L, LocalDateTime.now(), 7.0, 0.5, 100.0, 200.0, 150.0);

        QualityRecord qualityRecord = qualityService.checkWaterQuality(safeRecord);

        assertNotNull(qualityRecord);
        assertEquals("Green", qualityRecord.getQualityStatus());

        // Verify that it was saved in the database
        verify(repo, times(1)).save(any(QualityRecord.class));
    }

    /**
     * Test checking water quality when the record exceeds safe limits.
     */
    @Test
    void testCheckWaterQuality_NotSafe() {
        // Mock record with unsafe pH and TDS
        Records unsafeRecord = new Records(3L, LocalDateTime.now(), 9.0, 0.5, 400.0, 500.0, 300.0);

        QualityRecord qualityRecord = qualityService.checkWaterQuality(unsafeRecord);

        assertNotNull(qualityRecord);
        assertEquals("Red", qualityRecord.getQualityStatus());

        // Verify that it was saved in the database
        verify(repo, times(1)).save(any(QualityRecord.class));
    }

    /**
     * Test checking water quality when the record is null.
     */
    @Test
    void testCheckWaterQuality_NullRecord() {
        QualityRecord qualityRecord = qualityService.checkWaterQuality(null);
        assertNull(qualityRecord);

        // Verify that nothing was saved in the database
        verify(repo, never()).save(any(QualityRecord.class));
    }
}