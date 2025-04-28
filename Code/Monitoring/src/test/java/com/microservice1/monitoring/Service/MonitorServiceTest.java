package com.microservice1.monitoring.Service;

import com.microservice1.monitoring.Repository.Repo;
import com.microservice1.monitoring.entity.Records;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonitorServiceTest {

    @Mock
    private Repo repository;

    @InjectMocks
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLatestRecord_WhenQueueIsNotEmpty() {
        // Given: Add a record to the queue
        Records expectedRecord = new Records(1L, LocalDateTime.now(), 7.5, 0.1, 0.2, 0.3, 0.4);
        monitorService.queue.add(expectedRecord);

        // When: Calling getLatestRecord()
        Records actualRecord = monitorService.getLatestRecord();

        // Then: Ensure the returned record is correct and removed from queue
        assertNotNull(actualRecord);
        assertEquals(expectedRecord, actualRecord);
        assertTrue(monitorService.queue.isEmpty()); // Queue should be empty after polling
    }

    @Test
    void testGetLatestRecord_WhenQueueIsEmpty() {
        // When: Calling getLatestRecord() on an empty queue
        Records actualRecord = monitorService.getLatestRecord();

        // Then: Ensure null is returned
        assertNull(actualRecord);
    }

    @Test
    void testReadCsvAndSaveData_SuccessfulSave() throws Exception {
        // Mock CSV data
        String mockCsvData = "OBJECTID,PHpH,ALK_MGL,COND_USCM,BOD_MGL,NO2_N_MGL,CUSOL1_MGL,CUSOL2_UGL,FESOL1_UGL,ZN_SOL_UGL\n"
                + "1115,7.7,95.0,275.0,2.4,0.011,0.0022,0.76,146.51,5.0\n"
                + "1147,7.5,68.0,225.0,2.0,0.005,0.0011,1.36,720.25,5.0";

        // Create a CSVReader from mock data
        CSVReader mockCsvReader = new CSVReader(new StringReader(mockCsvData));

        // Spy on the MonitorService to override the CSV reading
        MonitorService spyService = spy(monitorService);
        doReturn(mockCsvReader).when(spyService).createCsvReader();

        // Execute the method
        spyService.readCsvAndSaveData();

        // Verify that the repository's save method was called twice (for 2 valid records)
        verify(repository, times(2)).save(any(Records.class));
    }
}
