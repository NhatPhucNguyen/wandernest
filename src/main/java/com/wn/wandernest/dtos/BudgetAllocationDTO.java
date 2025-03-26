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

    private double totalBudget;
    private double accommodation;
    private double food;
    private double activities;
    private double transportation;
    private double shopping;
    private double entertainment;
    private double other;

    private double remainingBudget;
    private double accommodationSpent;
    private double foodSpent;
    private double activitiesSpent;
    private double transportationSpent;
    private double shoppingSpent;
    private double entertainmentSpent;
    private double otherSpent;

    private List<ExpenseDTO> expenses;
}
