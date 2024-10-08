package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.repository.UserRepository;
import artem.gutkovskiy.financialaccounting.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final Cache<User> userCache;

    private static final String ALL = "all_users";
    @Autowired
    public UserService(UserRepository userRepository, Cache<User> userCache) {
        this.userRepository = userRepository;
        this.userCache = userCache;
    }

    public List<User> findAll() {
        logger.info("Получение всех пользователей");
        if (userCache.containsKey(ALL)) {
            logger.info("Извлечение всех пользователей из кэша");
            return userCache.get(ALL);
        } else {
            logger.info("Запрос к базе данных для" +
                    " получения всех пользователей");
            List<User> users = userRepository.findAll();
            if (!users.isEmpty()) {
                logger.info("Добавление всех пользователей в кэш");
                userCache.put(ALL, users);
            }
            return users;
        }
    }

    public Optional<User> findById(Long id) {
        logger.info("Поиск пользователя по ID: {}", id);
        if (userCache.containsKey(id.toString())) {
            logger.info("Извлечение пользователя по ID из кэша: {}", id);
            return userCache.get(id.toString()).
                    stream().filter(u -> u.getId().equals(id)).findFirst();
        } else {
            Optional<User> user = userRepository.findById(id);
            user.ifPresent(u -> {
                logger.info("Добавление пользователя по ID в кэш: {}", id);
                userCache.put(id.toString(), List.of(u));
            });
            return user;
        }
    }
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
    public User save(User user) {
        logger.info("Сохранение пользователя");

        User savedUser = userRepository.save(user);

        userCache.invalidate(ALL);
        userCache.invalidate(savedUser.getId().toString());

        return savedUser;
    }


    public void deleteById(Long id) {
        logger.info("Удаление пользователя по ID: {}", id);
        userRepository.findById(id).ifPresent(user -> {
            logger.info("Очистка кэша для пользователя по ID: {}", id);
            userCache.invalidate(ALL);
            userCache.invalidate(user.getId().toString());
            userRepository.deleteById(id);
        });
    }
}
