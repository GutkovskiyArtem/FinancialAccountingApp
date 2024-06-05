package artem.gutkovskiy.financialaccounting.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
}