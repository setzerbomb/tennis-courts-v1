package com.tenniscourts.reservations.reservations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.reservations.CreateReservationRequestDTO;
import com.tenniscourts.reservations.ReservationController;
import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.reservations.ReservationService;
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

@WebMvcTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ReservationController.class, ReservationService.class})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;
    private ObjectMapper objectMapper;
    private CreateReservationRequestDTO createReservationRequestDTO;
    private ReservationDTO reservationDTO;

    @Before
    public void init(){
        createReservationRequestDTO = CreateReservationRequestDTO.builder().scheduleId(1L).guestId(1L).build();
        reservationDTO = ReservationDTO.builder().id(1L).scheduledId(1L).guestId(1L).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldPass(){
        Assertions.assertTrue(true);
    }


}
