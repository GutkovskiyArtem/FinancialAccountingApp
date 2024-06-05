package artem.gutkovskiy.financialaccounting.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.expense;
public interface expense_repository extends JpaRepository<expense, Long> {
}