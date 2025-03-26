package com.wn.wandernest.dtos;

import java.time.LocalDateTime;

import com.wn.wandernest.enums.ExpenseCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
    private Long id;

    private String description; // e.g., "Hotel booking", "Dinner at a restaurant"
    private double amount;

    private ExpenseCategory category; // e.g., ACCOMMODATION, MEALS, ACTIVITIES, TRANSPORTATION, MISCELLANEOUS

    private LocalDateTime date; // Date of the expense
    private String itineraryDestination;
    private Long itineraryId;
}