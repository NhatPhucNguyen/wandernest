package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.RestaurantDTO;
import com.wn.wandernest.services.RestaurantApiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantApiClient restaurantApiClient;

    @GetMapping()
    public ResponseEntity<List<RestaurantDTO>> getRestaurantNearBy(@RequestParam Long itineraryId) {
        List<RestaurantDTO> restaurants = restaurantApiClient.fetchRestaurants(itineraryId);
        return ResponseEntity.ok(restaurants);
    }

    @PostMapping()
    public ResponseEntity<?> saveRestaurant(@RequestBody RestaurantDTO restaurantBody, @RequestParam Long itineraryId) {
        restaurantApiClient.saveRestaurantByItinerary(itineraryId, restaurantBody);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unsaveRestaurant(@PathVariable String id, @RequestParam Long itineraryId) {
        restaurantApiClient.unsaveRestaurantByItinerary(itineraryId, id);
        return ResponseEntity.ok().build();
    }
}
