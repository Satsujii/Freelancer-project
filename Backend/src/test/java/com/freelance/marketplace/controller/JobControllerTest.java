package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.JobService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;

@WebMvcTest(JobController.class)
class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JobService jobService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public JobService jobService() {
            return org.mockito.Mockito.mock(JobService.class);
        }
    }

    @Test
    @DisplayName("GET /api/jobs/public returns 200")
    void getAllPublicJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/jobs/public"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/jobs returns 200")
    void getAllJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/jobs/{id} returns 200")
    void getJob() throws Exception {
        when(jobService.getJob(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/jobs returns 200")
    void createJob() throws Exception {
        when(jobService.createJob(any(), anyLong())).thenReturn(null);
        mockMvc.perform(post("/api/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/jobs/{id} returns 200")
    void updateJob() throws Exception {
        when(jobService.updateJob(anyLong(), any(), anyLong())).thenReturn(null);
        mockMvc.perform(put("/api/jobs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/jobs/{id} returns 204")
    void deleteJob() throws Exception {
        doNothing().when(jobService).deleteJob(anyLong(), anyLong());
        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isNoContent());
    }
} 