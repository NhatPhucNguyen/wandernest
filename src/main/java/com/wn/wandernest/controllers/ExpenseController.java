package com.wn.wandernest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.ExpenseDTO;
import com.wn.wandernest.models.Expense;
import com.wn.wandernest.services.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Get all expenses for a specific itinerary.
     *
     * @param itineraryId The ID of the itinerary.
     * @return List of expenses.
     */
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpensesByItinerary(@RequestParam Long itineraryId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByItineraryAsDTO(itineraryId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Get a specific expense by its ID.
     *
     * @param expenseId The ID of the expense.
     * @return The Expense object.
     */
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long expenseId) {
        Expense expense = expenseService.getExpenseById(expenseId);
        return ResponseEntity.ok(expenseService.mapToDTO(expense));
    }

    /**
     * Create a new expense for a specific itinerary.
     *
     * @param itineraryId The ID of the itinerary.
     * @param expense     The Expense object to create.
     * @return The created Expense object.
     */
    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestParam Long itineraryId, @RequestBody Expense expense) {
        Expense createdExpense = expenseService.createExpense(itineraryId, expense);
        return ResponseEntity.ok(expenseService.mapToDTO(createdExpense));
    }

    /**
     * Update an existing expense.
     *
     * @param expenseId      The ID of the expense to update.
     * @param updatedExpense The updated Expense object.
     * @return The updated Expense object.
     */
    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long expenseId,
            @RequestBody Expense updatedExpense) {
        Expense expense = expenseService.updateExpense(expenseId, updatedExpense);
        return ResponseEntity.ok(expenseService.mapToDTO(expense));
    }

    /**
     * Delete an expense by its ID.
     *
     * @param expenseId The ID of the expense to delete.
     * @return A response indicating the deletion was successful.
     */
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }
}