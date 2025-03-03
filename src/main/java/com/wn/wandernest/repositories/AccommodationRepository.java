package com.wn.wandernest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wn.wandernest.models.Accommodation;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByItineraryId(Long id);

    Optional<Accommodation> findByIdAndItineraryId(String id, Long itineraryId);
}
