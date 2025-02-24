package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.dtos.ItineraryResponseDTO;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.services.ItineraryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateItinerary(HttpServletRequest request,
            @RequestBody ItineraryRequestDTO requestDTO) {
        Itinerary itinerary = itineraryService.generateItinerary(request, requestDTO);
        ItineraryResponseDTO itineraryResponseDTO = new ItineraryResponseDTO(itinerary);
        return ResponseEntity.ok(itineraryResponseDTO);
    }

    // TODO: Add more endpoints for itinerary management (e.g., get, update, delete
    // itineraries)
    @GetMapping()
    public ResponseEntity<?> getItineraryByUser(HttpServletRequest request) {
        List<ItineraryResponseDTO> response = itineraryService.getItinerariesByUser(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable Long id) {
        try {
            Itinerary itinerary = itineraryService.deleteItineraryById(id);
            return ResponseEntity.ok(new ItineraryResponseDTO(itinerary));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
