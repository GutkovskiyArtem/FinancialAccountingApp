package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.repository.ExpenseRepository;
import artem.gutkovskiy.financialaccounting.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseRepository expenseRepository;
    private final Cache<Expense> expenseCache;

    public ExpenseService(ExpenseRepository expenseRepository,
                          Cache<Expense> expenseCache) {
        this.expenseRepository = expenseRepository;
        this.expenseCache = expenseCache;
    }

    public List<Expense> findAll() {
        logger.info("Получение всех расходов");
        return expenseRepository.findAll();
    }

    public Optional<Expense> findById(Long id) {
        logger.info("Поиск расхода по ID: {}", id);
        return expenseRepository.findById(id);
    }

    public Expense save(Expense expense) {
        logger.info("Сохранение расхода для пользователя: {}",
                expense.getUserName());
        expenseCache.invalidate(expense.getUserName());
        return expenseRepository.save(expense);
    }

    public void deleteById(Long id) {
        logger.info("Удаление расхода по ID: {}", id);
        expenseRepository.findById(id).ifPresent(expense -> {
            logger.info("Очистка кэша для пользователя: {}",
                    expense.getUserName());
            expenseCache.invalidate(expense.getUserName());
            expenseRepository.deleteById(id);
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
            List<Expense> expenses =
                    expenseRepository.findExpensesByUserName(userName);
            if (!expenses.isEmpty()) {
                logger.info("Добавление данных в кэш для пользователя: {}",
                        userName);
                expenseCache.put(userName, expenses);
            }
            return expenses;
        }
    }
}
