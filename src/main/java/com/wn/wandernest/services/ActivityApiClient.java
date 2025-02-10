package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.ActivityApiConfig;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.models.Activity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityApiClient {
    private final RestTemplate restTemplate;
    private final ActivityApiConfig config;

    public List<Activity> fetchActivities(Location location, List<ActivityInterest> interests) {
        // Build URL with activity filters
        String interestQuery = interests.stream()
                .map(interest -> interest.name().toLowerCase())
                .collect(Collectors.joining(","));
        String url = String.format("%s?location=%s&interests=%s&key=%s",
                config.getBaseUrl(),
                URLEncoder.encode(location.toString(), StandardCharsets.UTF_8),
                interestQuery,
                config.getApiKey());

        // Call API
        ResponseEntity<PlacesResponse> response = restTemplate.getForEntity(url, PlacesResponse.class);

        // Map response to Activity entities
        return response.getBody().getResults().stream()
                .map(apiItem -> Activity.builder()
                        .name(apiItem.getName())
                        .priceLevel(apiItem.getPrice_level())
                        .build())
                .toList();
    }
}
