package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.ActivityDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.services.ActivityApiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityApiClient activityApiClient;

    @GetMapping()
    public ResponseEntity<List<ActivityDTO>> getActivities (Location location){
        List<ActivityDTO> activities = activityApiClient.fetchActivities(location, null);
        return ResponseEntity.ok(activities);
    }
}
