package com.tenniscourts.reservations.guests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.CreateOrUpdateGuestRequestDTO;
import com.tenniscourts.guests.GuestController;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.GuestService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.MediaType;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GuestController.class, GuestService.class})
public class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GuestService guestService;
    private ObjectMapper objectMapper;
    private CreateOrUpdateGuestRequestDTO createOrUpdateGuestRequestDTO;
    private GuestDTO guestDTO;

    @Before
    public void init(){
        createOrUpdateGuestRequestDTO = CreateOrUpdateGuestRequestDTO.builder().name("João").build();
        guestDTO = GuestDTO.builder().id(Long.valueOf(1)).name("João").build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create a guest and return status 201")
    public void insert() throws Exception {
        String content = objectMapper.writeValueAsString(createOrUpdateGuestRequestDTO);

        when(guestService.addGuest(createOrUpdateGuestRequestDTO)).thenReturn(guestDTO);

        this.mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update a guest and return status 200")
    public void update() throws Exception {
        createOrUpdateGuestRequestDTO.setName("Lucas");
        guestDTO.setName("Lucas");
        var json = objectMapper.writeValueAsString(createOrUpdateGuestRequestDTO);

        when(guestService.updateGuest(createOrUpdateGuestRequestDTO, Long.valueOf(1))).thenReturn(guestDTO);

        this.mockMvc.perform(put("/guests/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Lucas")));
    }

    @Test
    @DisplayName("Should delete the guest and return status 204")
    public void delete() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/guests/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return all the guests")
    public void list() throws Exception{
        when(guestService.findAllGuests()).thenReturn(List.of(guestDTO));

        this.mockMvc.perform(get("/guests/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("João")));
    }

    @Test
    @DisplayName("Should return all the guests that contains a specific string")
    public void findByName() throws Exception{
        when(guestService.findGuestsByName("o")).thenReturn(List.of(guestDTO));

        this.mockMvc.perform(get("/guests/search?name=o"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("João")));
    }

    @Test
    @DisplayName("Should find a guest by id")
    public void findById() throws Exception {
        when(guestService.findGuestById(Long.valueOf(1))).thenReturn(guestDTO);

        this.mockMvc.perform(get("/guests/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("João")));
    }

}
