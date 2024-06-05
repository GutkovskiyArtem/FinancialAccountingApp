package artem.gutkovskiy.financialaccounting.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.expense;
public interface expenseRepository extends JpaRepository<expense, Long> {
}