package artem.gutkovskiy.financialaccounting.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.user;
public interface userRepository extends JpaRepository<user, Long> {
}