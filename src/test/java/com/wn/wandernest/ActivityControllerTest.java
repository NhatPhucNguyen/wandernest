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

import com.wn.wandernest.controllers.ActivityController;
import com.wn.wandernest.dtos.ActivityDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.security.TokenBlacklist;
import com.wn.wandernest.services.ActivityApiClient;
import com.wn.wandernest.services.CustomUserDetailsService;
import com.wn.wandernest.utils.JwtTokenUtil;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {
    @MockitoBean
    private ActivityApiClient activityApiClient;

    @InjectMocks
    private ActivityController activityController;

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
    public void testGetActivities() throws Exception {
        Location location = new Location(48.8566, 2.3522);
        List<ActivityDTO> mockActivities = Arrays.asList(
                ActivityDTO.builder()
                        .id("exampleId")
                        .name("Eiffel Tower Visit")
                        .address("France")
                        .location(location)
                        .build());

        when(activityApiClient.fetchActivities(location, null)).thenReturn(mockActivities);

        mockMvc.perform(get("/api/activities")
                .param("lat", String.valueOf(location.getLat()))
                .param("lng", String.valueOf(location.getLng())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Eiffel Tower Visit"))
                .andExpect(jsonPath("$[0].address").value("France"))
                .andExpect(jsonPath("$[0].location.lat").value(48.8566))
                .andExpect(jsonPath("$[0].location.lng").value(2.3522));
    }
}
