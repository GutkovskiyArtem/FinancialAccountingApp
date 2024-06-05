package artem.gutkovskiy.financialaccounting.service;
import artem.gutkovskiy.financialaccounting.entity.expense;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class expenseService {
    private final artem.gutkovskiy.financialaccounting.repository.expenseRepository expenseRepository;

    public expenseService(artem.gutkovskiy.financialaccounting.repository.expenseRepository expenseRepository) {
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
