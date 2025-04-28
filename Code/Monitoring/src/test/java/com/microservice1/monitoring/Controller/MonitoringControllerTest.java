package com.microservice1.monitoring.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservice1.monitoring.Service.MonitorService;
import com.microservice1.monitoring.entity.Records;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MonitoringControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MonitorService monitorService;

    @InjectMocks
    private MonitoringController monitoringController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(monitoringController).build();
    }

    @Test
    void testGetNextMonitoringRecord_Success() throws Exception {
        // Mock a record
       Records mockRecord = new Records(1L, LocalDateTime.now(), 7.5, 0.002, 1.2, 5.5, 3.1);
     //   Records mockRecord =null;
                when(monitorService.getLatestRecord()).thenReturn(mockRecord);

        // Perform GET request and verify response
        mockMvc.perform(get("/get_record")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.ph").value(7.5)); //  Correct


        verify(monitorService, times(1)).getLatestRecord();
    }

    @Test
    void testGetNextMonitoringRecord_NoRecord() throws Exception {
    //    Records mockRecord = new Records(1L, LocalDateTime.now(), 7.5, 0.002, 1.2, 5.5, 3.1);
        when(monitorService.getLatestRecord()).thenReturn(null);

        mockMvc.perform(get("/get_record")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(monitorService, times(1)).getLatestRecord();
    }
}
