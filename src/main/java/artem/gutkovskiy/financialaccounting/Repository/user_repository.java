package artem.gutkovskiy.financialaccounting.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.user;
public interface user_repository extends JpaRepository<user, Long> {
}