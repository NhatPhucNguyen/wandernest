package com.wn.wandernest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wn.wandernest.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
