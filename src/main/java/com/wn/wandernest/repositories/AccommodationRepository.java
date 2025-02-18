package com.wn.wandernest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wn.wandernest.models.Accommodation;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

}
