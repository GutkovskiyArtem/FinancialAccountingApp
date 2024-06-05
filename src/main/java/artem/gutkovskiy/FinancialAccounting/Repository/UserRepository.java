package artem.gutkovskiy.FinancialAccounting.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.FinancialAccounting.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
}