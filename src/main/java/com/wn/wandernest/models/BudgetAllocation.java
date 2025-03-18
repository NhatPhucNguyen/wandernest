package com.wn.wandernest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//TODO: Complete budget allocation
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "budget_allocations")
public class BudgetAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double accommodation;
    private double meals;
    private double activities;
    private double transportation;
    private double miscellaneous;
    
    @OneToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
