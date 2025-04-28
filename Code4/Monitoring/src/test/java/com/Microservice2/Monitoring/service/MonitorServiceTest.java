package com.Microservice2.Monitoring.service;

import com.Microservice2.Monitoring.entity.Records;
import com.Microservice2.Monitoring.repo.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class MonitorServiceTest {

    @Mock
    private Repo repository;

    @InjectMocks
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test if records are processed and stored in the database correctly.
     */
    @Test
    void testProcessOneRecord() {
        // Create a mock record
        Records record = new Records(1L, LocalDateTime.now(), 7.5, 0.5, 20.0, 30.0, 40.0);

        // Add the record to the queue manually
        monitorService.processOneRecord(); // Should print "No new records to process."
        monitorService.recordQueue.add(record);

        // Process the record
        monitorService.processOneRecord();

        // Verify that the record was saved to the database
        verify(repository, times(1)).save(record);

        // Check if the record is now in the `recordSender` queue
        assertEquals(record, monitorService.getLatestRecord());
    }

    /**
     * Test fetching the latest record from the queue.
     */
    @Test
    void testGetLatestRecord() {
        Records record1 = new Records(1L, LocalDateTime.now(), 7.0, 0.5, 10.0, 20.0, 30.0);
        Records record2 = new Records(2L, LocalDateTime.now(), 6.8, 0.3, 12.0, 22.0, 35.0);

        monitorService.recordSender.add(record1);
        monitorService.recordSender.add(record2);

        // Fetch the latest record
        Records fetchedRecord = monitorService.getLatestRecord();
        assertNotNull(fetchedRecord);
        assertEquals(record1, fetchedRecord);

        // Fetch next record
        Records nextRecord = monitorService.getLatestRecord();
        assertNotNull(nextRecord);
        assertEquals(record2, nextRecord);

        // Queue should now be empty
        assertNull(monitorService.getLatestRecord());
    }

    /**
     * Test that an empty queue returns null.
     */
    @Test
    void testGetLatestRecordWhenQueueIsEmpty() {
        assertNull(monitorService.getLatestRecord());
    }

}