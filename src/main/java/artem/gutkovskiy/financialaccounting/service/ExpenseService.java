package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.cache.Cache;
import artem.gutkovskiy.financialaccounting.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(
            ExpenseService.class);
    private final ExpenseRepository expenseRepository;
    private final Cache<Expense> expenseCache;

    private static final String ALL_EXP = "all_expenses";

    public ExpenseService(ExpenseRepository expenseRepository,
                          Cache<Expense> expenseCache) {
        this.expenseRepository = expenseRepository;
        this.expenseCache = expenseCache;
    }

    public List<Expense> findAll() {
        logger.info("Получение всех расходов");
        if (expenseCache.containsKey(ALL_EXP)) {
            logger.info("Извлечение всех расходов из кэша");
            return expenseCache.get(ALL_EXP);
        } else {
            logger.info("Запрос к базе данных для получения всех расходов");
            List<Expense> expenses = expenseRepository.findAll();
            if (!expenses.isEmpty()) {
                logger.info("Добавление всех расходов в кэш");
                expenseCache.put(ALL_EXP, expenses);
            }
            return expenses;
        }
    }

    public Optional<Expense> findById(Long id) {
        logger.info("Поиск расхода по ID: {}", id);
        if (expenseCache.containsKey(id.toString())) {
            logger.info("Извлечение расхода по ID из кэша: {}", id);
            return expenseCache.get(id.toString()).stream().filter(u ->
                    u.getId().equals(id)).findFirst();
        } else {
            Optional<Expense> expense = expenseRepository.findById(id);
            expense.ifPresent(u -> {
                logger.info("Добавление расхода по ID в кэш: {}", id);
                expenseCache.put(id.toString(), List.of(u));
            });
            return expense;
        }
    }


    public Expense save(Expense expense) {
        logger.info("Сохранение расхода: {}", expense);

        expenseCache.invalidate(ALL_EXP);
        expenseCache.invalidate(expense.getUserName());

        if (expense.getId() != null) {
            expenseCache.invalidate(expense.getId().toString());
        }

        Expense savedExpense = expenseRepository.save(expense);

        logger.info("Добавление обновленного расхода в кэш: {}", savedExpense);
        List<Expense> allExpenses = expenseRepository.findAll();
        expenseCache.put(ALL_EXP, allExpenses);

        return savedExpense;
    }

    public void deleteById(Long id) {
        logger.info("Удаление расхода по ID: {}", id);
        expenseRepository.findById(id).ifPresent(expense -> {
            logger.info("Очистка кэша для ID: {}", id);
            expenseCache.invalidate(ALL_EXP);
            expenseCache.invalidate(id.toString());
            expenseCache.invalidate(expense.getUserName());
            expenseRepository.deleteById(id);

            List<Expense> allExpenses = expenseRepository.findAll();
            expenseCache.put(ALL_EXP, allExpenses);
        });
    }

    public List<Expense> findExpensesByUserName(String userName) {
        logger.info("Поиск расходов для пользователя: {}", userName);
        if (expenseCache.containsKey(userName)) {
            logger.info("Извлечение данных из кэша для пользователя: {}",
                    userName);
            return expenseCache.get(userName);
        } else {
            logger.info("Запрос к базе данных для пользователя: {}", userName);
            List<Expense> expenses = expenseRepository.findExpensesByUserName(
                    userName);
            if (!expenses.isEmpty()) {
                logger.info("Добавление данных в кэш для пользователя: {}",
                        userName);
                expenseCache.put(userName, expenses);
            }
            return expenses;
        }
    }
}
