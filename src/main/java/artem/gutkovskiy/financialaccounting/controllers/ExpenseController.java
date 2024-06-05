package artem.gutkovskiy.financialaccounting.controllers;

import artem.gutkovskiy.financialaccounting.service.ExpenseService;
import artem.gutkovskiy.financialaccounting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.entity.User;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

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
        if (expenseService.findById(id).isEmpty()) {
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
        if (expenseService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        expenseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
