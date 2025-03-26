package com.wn.wandernest.models;

import java.time.LocalDateTime;

import com.wn.wandernest.enums.ExpenseCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description; // e.g., "Hotel booking", "Dinner at a restaurant"
    private double amount;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category; // e.g., ACCOMMODATION, MEALS, ACTIVITIES, TRANSPORTATION, MISCELLANEOUS

    private LocalDateTime date; // Date of the expense

    @ManyToOne
    @JoinColumn(name = "budget_allocation_id")
    private BudgetAllocation budgetAllocation; // Link to the budget allocation
}