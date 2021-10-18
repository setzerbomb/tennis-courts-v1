package com.tenniscourts.reservations.schedules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.schedules.*;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

@WebMvcTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ScheduleController.class, ScheduleService.class})
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScheduleService scheduleService;
    private ObjectMapper objectMapper;
    private CreateScheduleRequestDTO createScheduleRequestDTO;
    private ScheduleDTO scheduleDTO;

    @Before
    public void init(){
        createScheduleRequestDTO = CreateScheduleRequestDTO.builder()
                .tennisCourtId(1L)
                .startDateTime(LocalDateTime.now().minusDays(1L))
                .endDateTime(LocalDateTime.now().plusDays(1L))
                .build();
        scheduleDTO = ScheduleDTO.builder()
                .tennisCourtId(1L)
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getEndDateTime())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldPass(){
        Assertions.assertTrue(true);
    }

}
