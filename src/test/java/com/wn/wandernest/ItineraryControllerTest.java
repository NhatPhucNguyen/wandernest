package com.wn.wandernest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wn.wandernest.controllers.ItineraryController;
import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.enums.Cuisine;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.TravelPreferences;
import com.wn.wandernest.security.TokenBlacklist;
import com.wn.wandernest.services.CustomUserDetailsService;
import com.wn.wandernest.services.ItineraryService;
import com.wn.wandernest.utils.JwtTokenUtil;

@WebMvcTest(ItineraryController.class)
public class ItineraryControllerTest {


    @MockitoBean
    private ItineraryService itineraryService;

    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenUtil jwtTokenUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private TokenBlacklist tokenBlacklist;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ItineraryController(itineraryService)).build();
    }

    @Test
    public void testGenerateItinerary() throws Exception {
        BudgetAllocation budgetAllocation = BudgetAllocation.builder()
            .accommodation(1000.00)
            .activities(500.00)
            .transportation(700.00)
            .build();

        TravelPreferences travelPreferences = TravelPreferences.builder()
            .accommodationType(AccommodationType.HOTEL)
            .activityInterests(List.of(ActivityInterest.BEACH))
            .cuisinePreferences(List.of(Cuisine.JAPANESE_RESTAURANT))
            .build();

        Itinerary itinerary = Itinerary.builder()
            .destination("Paris")
            .startDate(LocalDate.of(2023, 12, 1))
            .endDate(LocalDate.of(2023, 12, 10))
            .numberOfTravelers(2)
            .totalBudget(3000.00)
            .budgetAllocation(budgetAllocation)
            .travelPreferences(travelPreferences)
            .build();

        when(itineraryService.generateItinerary(any(ItineraryRequestDTO.class))).thenReturn(itinerary);

        ItineraryRequestDTO requestDTO = new ItineraryRequestDTO();
        requestDTO.setDestination("Paris");
        requestDTO.setStartDate(LocalDate.of(2023, 12, 1));
        requestDTO.setEndDate(LocalDate.of(2023, 12, 10));
        requestDTO.setNumberOfTravelers(2);
        requestDTO.setTotalBudget(3000.00);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/api/itineraries/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isCreated());
    }
}
