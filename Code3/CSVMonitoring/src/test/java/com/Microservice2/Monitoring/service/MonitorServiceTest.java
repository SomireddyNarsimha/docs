package com.Microservice2.Monitoring.service;

import com.Microservice2.Monitoring.entity.Records;
import com.Microservice2.Monitoring.repo.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     *  Test processing one record and storing it in the database.
     */
    @Test
    void testProcessOneRecord() {
        // Given: A record is manually added to the queue
        Records record = new Records(1L, LocalDateTime.now().toString(), 7.5, 0.5, 20.0, 30.0, 40.0);
        monitorService.recordQueue.add(record);

        // When: Processing one record
        monitorService.processOneRecord();

        // Then: The record should be saved in the database
        verify(repository, times(1)).save(record);

        // And: The record should move from `recordQueue` to `recordSender`
        assertTrue(monitorService.recordQueue.isEmpty());
        assertEquals(1, monitorService.recordSender.size());
    }

    /**
     *  Test fetching the latest unprocessed record.
     */
    @Test
    @Transactional
    void testGetLatest() {
        // Given: Mock an unprocessed record
        Records record = new Records(1L, LocalDateTime.now().toString(), 7.0, 0.5, 10.0, 20.0, 30.0);
        when(repository.findFirstByProcessedFalseOrderByTimestampAsc()).thenReturn(record);

        // When: Calling getLatest()
        Records latestRecord = monitorService.getLatest();

        // Then: The same record should be returned
        assertNotNull(latestRecord);
        assertEquals(record.getId(), latestRecord.getId());

        // And: It should be marked as processed in the database
        verify(repository, times(1)).markAsProcessed(record.getId());
    }

    /**
     * Test fetching the latest record when no unprocessed records exist.
     */
    @Test
    @Transactional
    void testGetLatest_NoRecords() {
        // Given: No unprocessed records available
        when(repository.findFirstByProcessedFalseOrderByTimestampAsc()).thenReturn(null);

        // When: Calling getLatest()
        Records latestRecord = monitorService.getLatest();

        // Then: It should return null
        assertNull(latestRecord);

        // And: It should NOT call markAsProcessed()
        verify(repository, never()).markAsProcessed(anyLong());
    }

    /**
     *  Test processing when queue is empty.
     */
    @Test
    void testProcessOneRecord_NoRecords() {
        // Given: An empty queue
        assertTrue(monitorService.recordQueue.isEmpty());

        // When: Processing a record
        monitorService.processOneRecord();

        // Then: No records should be saved
        verify(repository, never()).save(any(Records.class));
    }
}
