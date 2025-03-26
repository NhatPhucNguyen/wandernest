package com.wn.wandernest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wn.wandernest.models.BudgetAllocation;

@Repository
public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, Long> {

}
