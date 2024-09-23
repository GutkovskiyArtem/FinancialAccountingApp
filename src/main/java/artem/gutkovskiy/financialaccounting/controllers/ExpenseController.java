package artem.gutkovskiy.financialaccounting.controllers;

import artem.gutkovskiy.financialaccounting.counter.RequestCounter;
import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.service.ExpenseService;
import artem.gutkovskiy.financialaccounting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private static final String NOT_FOUND = "Расход с ID {} не найден";
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
        RequestCounter.getInstance().increment();
        return expenses;
    }

    @GetMapping("/by-username")
    public List<Expense> getExpensesByUserName(@RequestParam String userName) {
        RequestCounter.getInstance().increment();
        return expenseService.findExpensesByUserName(userName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        logger.info("Получен запрос на получение расхода с ID: {}", id);
        RequestCounter.getInstance().increment();
        return expenseService.findById(id)
                .map(expense -> {
                    logger.info("Расход с ID {} найден", id);
                    return ResponseEntity.ok(expense);
                })
                .orElseGet(() -> {
                    logger.warn(NOT_FOUND, id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @RequestBody Expense expense) {
        String name = expense.getUserName();
        logger.info("Получен запрос на создание расхода" +
                " для пользователя: {}", name);
        RequestCounter.getInstance().increment();
        try {
            User user = userService.findByName(name).orElseThrow(() -> {
                logger.error("Пользователь {} не найден", name);
                return new RuntimeException("User not found");
            });
            expense.setUser(user);
            expense.setUserName(user.getName());
            Expense savedExpense = expenseService.save(expense);
            logger.info("Расход создан с ID: {}", savedExpense.getId());
            return ResponseEntity.ok(savedExpense);
        } catch (Exception ex) {
            logger.error("Ошибка при создании расхода" +
                    " для пользователя {}: {}", name, ex.getMessage());

        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id,
                                                 @RequestBody Expense expense) {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на обновление расхода с ID: {}", id);

        if (expenseService.findById(id).isEmpty()) {
            logger.warn(NOT_FOUND, id);
            return ResponseEntity.notFound().build();
        }

        try {
            User user = expenseService.findById(id).get().getUser();

            if (user == null || user.getId() == null) {
                logger.error("Пользователь не указан в расходе");
                return ResponseEntity.badRequest().body(null);
            }

            expense.setId(id);
            expense.setUser(user);
            Expense updatedExpense = expenseService.save(expense);
            logger.info("Расход с ID {} успешно обновлен", id);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception ex) {
            logger.error("Ошибка при обновлении расхода с ID {}: {}", id,
                    ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(null);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на удаление расхода с ID: {}", id);
        if (expenseService.findById(id).isEmpty()) {
            logger.warn(NOT_FOUND, id);
            return ResponseEntity.notFound().build();
        }
        try {
            expenseService.deleteById(id);
            logger.info("Расход с ID {} успешно удален", id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            logger.error("Ошибка при удалении расхода с ID {}: {}",
                    id, ex.getMessage());

        }
        return null;
    }
    @PostMapping("/trigger-bad-request")
    public ResponseEntity<String> triggerBadRequest(
            @RequestParam(required = false) String param) {
        RequestCounter.getInstance().increment();
            throw new IllegalArgumentException("Simulated bad request");
    }

}
