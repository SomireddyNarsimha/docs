package com.QualityCheck.Quality.Controller;
import com.QualityCheck.Quality.Dto.Records;
import com.QualityCheck.Quality.Service.QualityService;
import com.QualityCheck.Quality.entity.QualityRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;


class QualityControllerTest {

    @Mock
    private QualityService qualityService;

    @InjectMocks
    private QualityController qualityController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(qualityController).build();
    }

    /**
     * Test the /stream endpoint to start the quality check streaming.
     */
    @Test
    void testStreamQualityCheck() throws Exception {
        mockMvc.perform(get("/quality/stream")
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk());
    }

    /**
     * Test the /stop endpoint to stop the quality check process.
     */
    @Test
    void testStopQualityCheck() throws Exception {
        mockMvc.perform(get("/quality/stop"))
                .andExpect(status().isOk());
    }
}