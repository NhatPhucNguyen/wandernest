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

import com.wn.wandernest.dtos.ActivityDTO;
import com.wn.wandernest.services.ActivityApiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityApiClient activityApiClient;

    @GetMapping()
    public ResponseEntity<List<ActivityDTO>> getActivities(@RequestParam Long itineraryId) {
        List<ActivityDTO> activities = activityApiClient.fetchActivities(itineraryId);
        return ResponseEntity.ok(activities);
    }

    @PostMapping()
    public ResponseEntity<?> saveActivity(@RequestBody ActivityDTO activityBody, @RequestParam Long itineraryId) {
        activityApiClient.saveActivityByItinerary(itineraryId, activityBody);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unsaveActivity(@PathVariable String id, @RequestParam Long itineraryId) {
        activityApiClient.unsaveActivityByItinerary(itineraryId, id);
        return ResponseEntity.ok().build();
    }
}