package com.tenniscourts.reservations.tennisCourts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.MediaType;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TennisCourtController.class, TennisCourtService.class})
public class TennisCourtControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TennisCourtService tennisCourtService;
    private ObjectMapper objectMapper;

    private TennisCourtDTO tennisCourtDTO;

    @Before
    public void init(){
        tennisCourtDTO = TennisCourtDTO.builder().id(1L).name("Tennis Court 01").build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create a tennisCourt and return status 201")
    @Ignore
    public void insert() throws Exception {
        String content = objectMapper.writeValueAsString(tennisCourtDTO);

        when(tennisCourtService.addTennisCourt(tennisCourtDTO)).thenReturn(tennisCourtDTO);

        this.mockMvc.perform(post("/tennis-courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should find a tennisCourt by id")
    public void findById() throws Exception {
        when(tennisCourtService.findTennisCourtById(Long.valueOf(1))).thenReturn(tennisCourtDTO);

        this.mockMvc.perform(get("/tennis-courts/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Tennis Court 01")));
    }

    @Test
    @DisplayName("Should find a tennisCourt by id with schedules")
    public void findByIdWithSchedules() throws Exception {
        tennisCourtDTO.setTennisCourtSchedules(List.of(ScheduleDTO.builder().id(1L).build()));
        when(tennisCourtService.findTennisCourtWithSchedulesById(Long.valueOf(1))).thenReturn(tennisCourtDTO);

        this.mockMvc.perform(get("/tennis-courts/1/schedules"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Tennis Court 01")))
                .andExpect(jsonPath("tennisCourtSchedules.size()", Matchers.is(1)));
    }


}
