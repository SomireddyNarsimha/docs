package com.microservice2.Quality.controller;

import com.microservice2.Quality.service.QualityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QualityController.class) //  Loads only the controller and mocks dependencies
@ExtendWith(MockitoExtension.class)  //  Enables Mockito extensions
class QualityControllerTest {

    @Autowired
    private MockMvc mockMvc; //  Simulates HTTP requests

    @MockBean
    private QualityService qualityService; // Mocks the service layer

    @Test
    void testStartQualityCheck() throws Exception {
        //  Mock an SseEmitter response
        SseEmitter mockEmitter = new SseEmitter();
        when(qualityService.startContinuousQualityCheck()).thenReturn(mockEmitter);

        mockMvc.perform(MockMvcRequestBuilders.get("/quality/start-quality-check"))
                .andExpect(status().isOk());

        verify(qualityService, times(1)).startContinuousQualityCheck();
    }

    @Test
    void testStopQualityCheck() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/quality/stop-quality-check"))
                .andExpect(status().isOk())
                .andExpect(content().string("Water Quality Check Stopped."));

        verify(qualityService, times(1)).stopQualityCheck();
    }
}
