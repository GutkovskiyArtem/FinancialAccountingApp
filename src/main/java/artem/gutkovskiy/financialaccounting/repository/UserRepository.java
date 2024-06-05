package artem.gutkovskiy.financialaccounting.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
}