package com.wn.wandernest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.User;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByUser(User user);
}
