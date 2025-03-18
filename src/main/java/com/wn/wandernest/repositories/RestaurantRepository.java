package com.wn.wandernest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wn.wandernest.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByItineraryId(Long itineraryId);

    Optional<Restaurant> findByIdAndItineraryId(String id, Long itineraryId);
}
