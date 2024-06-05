package artem.gutkovskiy.financialaccounting.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import artem.gutkovskiy.financialaccounting.Service.expense_service;
import artem.gutkovskiy.financialaccounting.Service.user_service;
import artem.gutkovskiy.financialaccounting.entity.expense;
import artem.gutkovskiy.financialaccounting.entity.user;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class Expense_controller {

    private final expense_service expenseService;

    private final user_service userService;

    public Expense_controller(expense_service expenseService, user_service userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping
    public List<expense> getAllExpenses() {
        return expenseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<expense> getExpenseById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<expense> createExpense(@RequestBody expense expense, @RequestParam("userId") Long userId) {
        user user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        expense.setUserName(user.getName());
        artem.gutkovskiy.financialaccounting.entity.expense savedExpense = expenseService.save(expense);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<expense> updateExpense(@PathVariable Long id, @RequestBody expense expense, @RequestParam("userId") Long userId) {
        if (expenseService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        user user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        expense.setId(id);
        artem.gutkovskiy.financialaccounting.entity.expense updatedExpense = expenseService.save(expense);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        if (expenseService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        expenseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
