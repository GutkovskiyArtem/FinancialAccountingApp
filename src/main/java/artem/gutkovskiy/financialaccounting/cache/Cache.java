package artem.gutkovskiy.financialaccounting.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache<T> {
    private static final Logger logger = LoggerFactory.getLogger(Cache.class);

    private final Map<String, List<T>> myCache = new ConcurrentHashMap<>();


    public boolean containsKey(String key) {
        return myCache.containsKey(key);
    }


    public List<T> get(String key) {
        logger.info("Извлечение данных из кэша для ключа: {}", key);
        return myCache.get(key);
    }


    public void put(String key, List<T> items) {
        logger.info("Добавление данных в кэш для ключа: {}", key);
        myCache.put(key, items);
    }


    public void invalidate(String key) {
        logger.info("Очистка кэша для ключа: {}", key);
        myCache.remove(key);
    }


    public void invalidateAll() {
        logger.info("Полная очистка кэша");
        myCache.clear();
    }
}
