package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.AccommodationDTO;
import com.wn.wandernest.services.AccommodationApiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accommodations")
@CrossOrigin
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationApiClient accommodationApiClient;

    @GetMapping()
    // get accommodation by Itinerary ID
    public ResponseEntity<List<AccommodationDTO>> getAccommodationNearBy(@RequestParam Long itineraryId) {
        List<AccommodationDTO> accommodations = accommodationApiClient.fetchAccommodations(itineraryId);
        return ResponseEntity.ok(accommodations);
    }

    @PostMapping()
    public ResponseEntity<?> saveAccommodation(@RequestBody AccommodationDTO accommodationBody,
            @RequestParam Long itineraryId) {
        accommodationApiClient.saveAccommodationByItinerary(itineraryId, accommodationBody);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unsaveAccommodation(@PathVariable String id, @RequestParam Long itineraryId) {
        accommodationApiClient.unsaveAccommodationByItinerary(itineraryId, id);
        return ResponseEntity.ok().build();
    }
}
