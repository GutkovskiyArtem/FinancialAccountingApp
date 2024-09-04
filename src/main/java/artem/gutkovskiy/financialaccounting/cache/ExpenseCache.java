package artem.gutkovskiy.financialaccounting.cache;

import artem.gutkovskiy.financialaccounting.entity.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExpenseCache {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseCache.class);

    private final Map<String, List<Expense>> cache = new HashMap<>();

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public List<Expense> get(String key) {
        logger.info("Извлечение данных из кэша для пользователя: {}", key);
        return cache.get(key);
    }

    public void put(String key, List<Expense> expenses) {
        logger.info("Добавление данных в кэш для пользователя: {}", key);
        cache.put(key, expenses);
    }

    public void invalidate(String key) {
        logger.info("Очистка кэша для пользователя: {}", key);
        cache.remove(key);
    }
}
