package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.AccommodationDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.services.AccommodationApiClient;
@RestController
@RequestMapping("/api/accommodations")
@CrossOrigin
public class AccommodationController {
    private final AccommodationApiClient accommodationApiClient;

    public AccommodationController(AccommodationApiClient accommodationApiClient) {
        this.accommodationApiClient = accommodationApiClient;
    }

    @GetMapping()
    public ResponseEntity<List<AccommodationDTO>> getAccommodationNearBy(Location location) {
        List<AccommodationDTO> accommodations = accommodationApiClient.fetchAccommodations(location, null, 0);
        return ResponseEntity.ok(accommodations);
    }
}
