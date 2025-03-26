package com.wn.wandernest.models;

import java.util.List;

import com.wn.wandernest.enums.ExpenseCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private double totalBudget;
    private double accommodation;
    private double food;
    private double activities;
    private double transportation;
    private double shopping;
    private double entertainment;
    private double other;

    private String currency;

    @OneToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @OneToMany(mappedBy = "budgetAllocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses;

    public double calculateRemainingBudget() {
        return totalBudget - calculateTotalSpent();
    }

    // Calculate total spent dynamically
    public double calculateTotalSpent() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    // Helper method to calculate spent by category
    public double calculateSpentByCategory(ExpenseCategory category) {
        return expenses.stream()
                .filter(expense -> expense.getCategory() == category)
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}