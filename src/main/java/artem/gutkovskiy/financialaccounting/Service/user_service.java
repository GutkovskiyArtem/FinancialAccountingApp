package artem.gutkovskiy.financialaccounting.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import artem.gutkovskiy.financialaccounting.Repository.user_repository;
import artem.gutkovskiy.financialaccounting.entity.user;

import java.util.List;
import java.util.Optional;

@Service
public class user_service {
    private final user_repository userRepository;

    @Autowired
    public user_service(user_repository userRepository) {
        this.userRepository = userRepository;
    }

    public List<user> findAll() {
        return userRepository.findAll();
    }

    public Optional<user> findById(Long id) {
        return userRepository.findById(id);
    }

    public user save(user user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
