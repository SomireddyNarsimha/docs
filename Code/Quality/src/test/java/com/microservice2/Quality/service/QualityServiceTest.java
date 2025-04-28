package com.microservice2.Quality.service;

import com.microservice2.Quality.dto.MonitoringRecordDTO;
import com.microservice2.Quality.entity.QualityData;
import com.microservice2.Quality.monitoringClient.MonitoringClient;
import com.microservice2.Quality.repo.QualityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QualityServiceTest {

    @Mock
    private MonitoringClient monitoringClient;

    @Mock
    private QualityRepo qualityRepo;

    @InjectMocks
    private QualityService qualityService;

    @BeforeEach
    void setUp() {
        qualityService = new QualityService(monitoringClient, qualityRepo);
    }

    @Test
    void testStartContinuousQualityCheck() {
        SseEmitter emitter = qualityService.startContinuousQualityCheck();
        assertNotNull(emitter); // Ensure emitter is created
    }

    @Test
    void testSendDataToClients() {
        // Given: Mock monitoring data
        MonitoringRecordDTO mockRecord = new MonitoringRecordDTO(1L, 7.0, "","223233423");
        QualityData expectedQualityData = new QualityData(1L, 7.0, "Green-Flag","223233423");

        when(monitoringClient.getNextMonitoringRecord()).thenReturn(mockRecord);
        when(qualityRepo.save(any(QualityData.class))).thenReturn(null);  // Correct


        // When: Start monitoring process
        qualityService.startContinuousQualityCheck();

        // Then: Verify that data is processed and saved
        verify(qualityRepo, timeout(2000)).save(any(QualityData.class));
    }

    @Test
    void testStopQualityCheck() throws NoSuchFieldException, IllegalAccessException {
        // Start the service
        qualityService.startContinuousQualityCheck();

        // Stop the service
        qualityService.stopQualityCheck();

        // Ensure background process has stopped
        assertFalse(isServiceRunning(qualityService), "Quality service should not be running after stopping");
    }

    // Helper method to check if the service is running
    private boolean isServiceRunning(QualityService service) throws NoSuchFieldException, IllegalAccessException {
        Field runningField = QualityService.class.getDeclaredField("running");
        runningField.setAccessible(true);
        return (boolean) runningField.get(service);
    }

}
