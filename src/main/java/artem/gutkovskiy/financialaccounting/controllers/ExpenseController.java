package artem.gutkovskiy.financialaccounting.controllers;

import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.service.ExpenseService;
import artem.gutkovskiy.financialaccounting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;

    private final UserService userService;

    public ExpenseController(ExpenseService expenseService,
                             UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        logger.info("Получен запрос на получение всех расходов");
        List<Expense> expenses = expenseService.findAll();
        logger.info("Найдено {} расходов", expenses.size());
        return expenses;
    }

    @GetMapping("/by-username")
    public List<Expense> getExpensesByUserName(@RequestParam String userName) {
        logger.info("Получен запрос на получение расходов" +
                " для пользователя: {}", userName);
        List<Expense> expenses =
                expenseService.findExpensesByUserName(userName);
        logger.info("Найдено {} расходов для пользователя {}",
                expenses.size(), userName);
        return expenses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        logger.info("Получен запрос на получение расхода с ID: {}", id);
        return expenseService.findById(id)
                .map(expense -> {
                    logger.info("Расход с ID {} найден", id);
                    return ResponseEntity.ok(expense);
                })
                .orElseGet(() -> {
                    logger.warn("Расход с ID {} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @RequestBody Expense expense, @RequestParam("userId") Long userId) {
        logger.info("Получен запрос на создание расхода" +
                " для пользователя с ID: {}", userId);
        try {
            User user = userService.findById(userId).orElseThrow(() -> {
                logger.error("Пользователь с ID {} не найден", userId);
                return new RuntimeException("User not found");
            });
            expense.setUser(user);
            expense.setUserName(user.getName());
            Expense savedExpense = expenseService.save(expense);
            logger.info("Расход создан с ID: {}", savedExpense.getId());
            return ResponseEntity.ok(savedExpense);
        } catch (Exception ex) {
            logger.error("Ошибка при создании расхода" +
                    " для пользователя с ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id,
                                                 @RequestBody Expense expense,
                                                 @RequestParam("userId")
                                                     Long userId) {
        logger.info("Получен запрос на обновление расхода с ID: {}", id);
        if (expenseService.findById(id).isEmpty()) {
            logger.warn("Расход с ID {} не найден", id);
            return ResponseEntity.notFound().build();
        }

        try {
            User user = userService.findById(userId).orElseThrow(() -> {
                logger.error("Пользователь с ID {} не найден", userId);
                return new RuntimeException("User not found");
            });
            expense.setUser(user);
            expense.setId(id);
            Expense updatedExpense = expenseService.save(expense);
            logger.info("Расход с ID {} успешно обновлен", id);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception ex) {
            logger.error("Ошибка при обновлении расхода с ID {}: {}",
                    id, ex.getMessage());
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        logger.info("Получен запрос на удаление расхода с ID: {}", id);
        if (expenseService.findById(id).isEmpty()) {
            logger.warn("Расход с ID {} не найден", id);
            return ResponseEntity.notFound().build();
        }
        try {
            expenseService.deleteById(id);
            logger.info("Расход с ID {} успешно удален", id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            logger.error("Ошибка при удалении расхода с ID {}: {}",
                    id, ex.getMessage());
            throw ex;
        }
    }
    @PostMapping("/trigger-bad-request")
    public ResponseEntity<String> triggerBadRequest(
            @RequestParam(required = false) String param) {
            throw new IllegalArgumentException("Simulated bad request");
    }

}
