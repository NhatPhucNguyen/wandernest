package com.wn.wandernest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wn.wandernest.models.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByItineraryId(Long itineraryId);

    Optional<Activity> findByIdAndItineraryId(String id, Long itineraryId);
}