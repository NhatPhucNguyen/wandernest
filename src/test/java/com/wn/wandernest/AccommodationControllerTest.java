package com.wn.wandernest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.wn.wandernest.controllers.AccommodationController;
import com.wn.wandernest.dtos.AccommodationDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.security.TokenBlacklist;
import com.wn.wandernest.services.AccommodationApiClient;
import com.wn.wandernest.services.CustomUserDetailsService;
import com.wn.wandernest.utils.JwtTokenUtil;

@WebMvcTest(AccommodationController.class)
public class AccommodationControllerTest {
    @MockitoBean
    private AccommodationApiClient accommodationApiClient;

    @InjectMocks
    private AccommodationController accommodationController;

    @MockitoBean
    private JwtTokenUtil jwtTokenUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private TokenBlacklist tokenBlacklist;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetAccommodationNearBy() throws Exception {
        Location location = new Location(48.8566, 2.3522);
        List<AccommodationDTO> mockAccommodations = Arrays.asList(
                AccommodationDTO.builder()
                        .id("exampleId")
                        .name("Hotel Paris")
                        .address("123 Paris St")
                        .priceLevel("Moderate")
                        .location(location)
                        .types(Arrays.asList(AccommodationType.HOTEL))
                        .build());

        when(accommodationApiClient.fetchAccommodations(location, null, 0)).thenReturn(mockAccommodations);

        mockMvc.perform(get("/api/accommodations")
                .param("lat", String.valueOf(location.getLat()))
                .param("lng", String.valueOf(location.getLng())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hotel Paris"))
                .andExpect(jsonPath("$[0].address").value("123 Paris St"))
                .andExpect(jsonPath("$[0].priceLevel").value("Moderate"))
                .andExpect(jsonPath("$[0].location.lat").value(48.8566))
                .andExpect(jsonPath("$[0].location.lng").value(2.3522))
                .andExpect(jsonPath("$[0].types[0]").value("HOTEL"));
    }
}
