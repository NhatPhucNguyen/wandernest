package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.ActivityApiConfig;
import com.wn.wandernest.dtos.TripAdvisorActivityResponse;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.enums.ActivityType;
import com.wn.wandernest.models.Activity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityApiClient {
    private final RestTemplate restTemplate;
    private final ActivityApiConfig config;
    
    public List<Activity> fetchActivities(String destination, List<ActivityInterest> interests) {
        // Build URL with activity filters
        String interestQuery = interests.stream()
            .map(interest -> interest.name().toLowerCase())
            .collect(Collectors.joining(","));

        String url = String.format("%s?location=%s&interests=%s&key=%s",
            config.getBaseUrl(),
            URLEncoder.encode(destination, StandardCharsets.UTF_8),
            interestQuery,
            config.getApiKey()
        );

        // Call API
        ResponseEntity<TripAdvisorActivityResponse> response = restTemplate.getForEntity(url, TripAdvisorActivityResponse.class);

        // Map response to Activity entities
        return response.getBody().getData().stream()
            .map(apiItem -> Activity.builder()
                .name(apiItem.getName())
                .type(ActivityType.valueOf(apiItem.getCategory().toUpperCase()))
                .cost(apiItem.getPrice())
                .location(apiItem.getAddress())
                .build())
            .toList();
    }
}
