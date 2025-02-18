package com.wn.wandernest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wn.wandernest.models.Activity;

public interface ActivityRepository extends JpaRepository<Activity,Long>{
    
}
