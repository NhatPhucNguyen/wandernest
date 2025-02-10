package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.RestaurantApiConfig;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.Cuisine;
import com.wn.wandernest.models.Restaurant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestaurantApiClient {
    private final RestTemplate restTemplate;
    private final RestaurantApiConfig config;

    public List<Restaurant> fetchRestaurants(Location location, List<Cuisine> cuisines) {
        // Build URL with cuisine filters
        String cuisineQuery = cuisines.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        String url = String.format("%s?location=%s&cuisines=%s&key=%s",
                config.getBaseUrl(),
                URLEncoder.encode(location.toString(), StandardCharsets.UTF_8),
                cuisineQuery,
                config.getApiKey());

        // Call TripAdvisor API
        ResponseEntity<PlacesResponse> response = restTemplate.getForEntity(url, PlacesResponse.class);

        // Map response to Restaurant entities
        return response.getBody().getResults().stream()
                .map(apiItem -> Restaurant.builder()
                        .name(apiItem.getName())
                        .priceLevel(apiItem.getPrice_level())
                        .build())
                .toList();
    }
}
