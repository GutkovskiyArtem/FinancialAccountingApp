package artem.gutkovskiy.financialaccounting.controllers;

import artem.gutkovskiy.financialaccounting.counter.RequestCounter;
import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private static final String NOT_FOUND = "Пользователь с ID {} не найден";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на получение всех пользователей");
        List<User> users = userService.findAll();
        logger.info("Найдено {} пользователей", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на получение пользователя с ID: {}", id);
        return userService.findById(id)
                .map(user -> {
                    logger.info("Пользователь с ID {} найден", id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn(NOT_FOUND, id);
                    return ResponseEntity.notFound().build();
                });
    }


    @PostMapping
    public User createUser(@RequestBody User user) {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на создание пользователя");
        User createdUser = userService.save(user);
        logger.info("Пользователь создан с ID: {}", createdUser.getId());
        return createdUser;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id, @RequestBody User user) {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на обновление пользователя");
        if (userService.findById(id).isEmpty()) {
            logger.warn(NOT_FOUND, id);
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        User updatedUser = userService.save(user);
        logger.info("Пользователь успешно обновлен");
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        RequestCounter.getInstance().increment();
        logger.info("Получен запрос на удаление пользователя с ID: {}", id);
        if (userService.findById(id).isEmpty()) {
            logger.warn(NOT_FOUND, id);
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        logger.info("Пользователь с ID {} успешно удален", id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/trigger-internal-server-error")
    public ResponseEntity<String> triggerInternalServerError() {
        RequestCounter.getInstance().increment();
        throw new NullPointerException("Simulated internal server error");
    }

}
