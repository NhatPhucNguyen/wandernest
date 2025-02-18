package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.RestaurantDTO;
import com.wn.wandernest.services.RestaurantApiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantApiClient restaurantApiClient;

    @GetMapping()
    public ResponseEntity<List<RestaurantDTO>> getRestaurantNearBy(Location location){
        List<RestaurantDTO> restaurants = restaurantApiClient.fetchRestaurants(location, null);
        return ResponseEntity.ok(restaurants);
    }
}
