package com.wn.wandernest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wn.wandernest.models.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
