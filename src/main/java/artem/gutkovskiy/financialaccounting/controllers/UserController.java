package artem.gutkovskiy.financialaccounting.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import artem.gutkovskiy.financialaccounting.entity.user;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final artem.gutkovskiy.financialaccounting.service.userService userService;

    public UserController(artem.gutkovskiy.financialaccounting.service.userService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<user> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<user> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public user createUser(@RequestBody user user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<user> updateUser(@PathVariable Long id, @RequestBody user user) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}