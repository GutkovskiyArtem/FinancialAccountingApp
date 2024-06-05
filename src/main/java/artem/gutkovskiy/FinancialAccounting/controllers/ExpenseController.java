package artem.gutkovskiy.FinancialAccounting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import artem.gutkovskiy.FinancialAccounting.Service.ExpenseService;
import artem.gutkovskiy.FinancialAccounting.Service.UserService;
import artem.gutkovskiy.FinancialAccounting.entity.Expense;
import artem.gutkovskiy.FinancialAccounting.entity.User;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense, @RequestParam("userId") Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        expense.setUserName(user.getName());
        Expense savedExpense = expenseService.save(expense);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense, @RequestParam("userId") Long userId) {
        if (!expenseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        expense.setId(id);
        Expense updatedExpense = expenseService.save(expense);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        if (!expenseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        expenseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
