package com.wn.wandernest.controllers;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.dtos.ItineraryResponseDTO;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.enums.Cuisine;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.services.ItineraryService;

@RestController
@CrossOrigin
@RequestMapping("/api/itineraries")
public class ItineraryController {
    public ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping("generate")
    public ResponseEntity<ItineraryResponseDTO> generateItinerary() {
        ItineraryRequestDTO itineraryRequestDTO = new ItineraryRequestDTO();
        itineraryRequestDTO.setDestination("Paris");
        itineraryRequestDTO.setStartDate(LocalDate.of(2023, 12, 1));
        itineraryRequestDTO.setEndDate(LocalDate.of(2023, 12, 10));
        itineraryRequestDTO.setNumberOfTravelers(2);
        itineraryRequestDTO.setTotalBudget(3000.00);
        itineraryRequestDTO.setAccommodationType(AccommodationType.HOTEL);
        itineraryRequestDTO.setCuisinePreferences(Arrays.asList(Cuisine.ITALIAN, Cuisine.JAPANESE));
        itineraryRequestDTO.setActivityInterests(Arrays.asList(ActivityInterest.SIGHTSEEING, ActivityInterest.CULTURAL));
        Itinerary itinerary = itineraryService.generateItinerary(itineraryRequestDTO);
        ItineraryResponseDTO itineraryResponseDTO = new ItineraryResponseDTO(itinerary);
        return ResponseEntity.ok(itineraryResponseDTO);
    }
}
