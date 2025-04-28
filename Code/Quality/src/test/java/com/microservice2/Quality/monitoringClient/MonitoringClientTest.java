package com.microservice2.Quality.monitoringClient;

import static org.junit.jupiter.api.Assertions.*;


import com.microservice2.Quality.dto.MonitoringRecordDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MonitoringClientTest {

    @Mock
    private MonitoringClient monitoringClient;

    @Test
    void testGetNextMonitoringRecord() {
        MonitoringRecordDTO mockRecord = new MonitoringRecordDTO(1L, 7.2, "Green-Flag","223334433");
        when(monitoringClient.getNextMonitoringRecord()).thenReturn(mockRecord);

        MonitoringRecordDTO result = monitoringClient.getNextMonitoringRecord();

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(7.2, result.getPh());
        assertEquals("Green-Flag", result.getQualityStatus());
        assertEquals("223334433",result.getUuid());
    }
}