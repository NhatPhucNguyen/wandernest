package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.dtos.ItineraryResponseDTO;
import com.wn.wandernest.dtos.ItineraryStatusUpdateDTO;
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
    public ResponseEntity<?> generateItinerary(@RequestBody ItineraryRequestDTO requestDTO) {
        Itinerary itinerary = itineraryService.generateItinerary(requestDTO);
        ItineraryResponseDTO itineraryResponseDTO = new ItineraryResponseDTO(itinerary);
        return ResponseEntity.status(201).body(itineraryResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<?> getItineraryByUser(HttpServletRequest request) {
        List<ItineraryResponseDTO> response = itineraryService.getItinerariesByUser(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable Long id) {
        try {
            itineraryService.deleteItineraryById(id);
            return ResponseEntity.ok("Itinerary deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateItineraryStatus(@PathVariable Long id,
            @RequestBody ItineraryStatusUpdateDTO request) {
        try {
            Itinerary itinerary = itineraryService.updateItineraryStatus(id, request.getStatus());
            return ResponseEntity.ok(new ItineraryResponseDTO(itinerary));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
