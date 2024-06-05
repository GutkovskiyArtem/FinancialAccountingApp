package artem.gutkovskiy.financialaccounting.Service;
import artem.gutkovskiy.financialaccounting.Repository.expense_repository;
import artem.gutkovskiy.financialaccounting.entity.expense;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class expense_service {
    private final expense_repository expenseRepository;

    public expense_service(expense_repository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<expense> findAll() {
        return expenseRepository.findAll();
    }

    public Optional<expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public expense save(expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }
}
