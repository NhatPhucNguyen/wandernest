package com.wn.wandernest.dtos;

import com.wn.wandernest.models.BudgetAllocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class BudgetAllocationDTO {
    private double accommodation;
    private double meals;
    private double activities;
    private double transportation;
    private double miscellaneous;
    public BudgetAllocationDTO(BudgetAllocation budgetAllocation) {
        this.accommodation = budgetAllocation.getAccommodation();
        this.meals = budgetAllocation.getMeals();
        this.activities = budgetAllocation.getActivities();
        this.transportation = budgetAllocation.getTransportation();
        this.miscellaneous = budgetAllocation.getMiscellaneous();
    }
}
