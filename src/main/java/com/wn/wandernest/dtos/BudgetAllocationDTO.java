package com.wn.wandernest.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetAllocationDTO {
    private Long id;

    private Double totalBudget;
    private Double accommodation;
    private Double food;
    private Double activities;
    private Double transportation;
    private Double shopping;
    private Double entertainment;
    private Double other;

    private Double remainingBudget;
    private Double accommodationSpent;
    private Double foodSpent;
    private Double activitiesSpent;
    private Double transportationSpent;
    private Double shoppingSpent;
    private Double entertainmentSpent;
    private Double otherSpent;

    private List<ExpenseDTO> expenses;
}
