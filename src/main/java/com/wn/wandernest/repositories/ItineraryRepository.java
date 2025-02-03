package com.wn.wandernest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wn.wandernest.models.Itinerary;
@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

}
