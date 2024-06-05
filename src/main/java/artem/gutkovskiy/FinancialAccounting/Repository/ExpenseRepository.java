package artem.gutkovskiy.FinancialAccounting.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import artem.gutkovskiy.FinancialAccounting.entity.Expense;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}