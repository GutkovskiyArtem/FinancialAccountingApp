package artem.gutkovskiy.financialaccounting.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.financialaccounting.entity.Expense;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}