    package artem.gutkovskiy.financialaccounting.service;

    import artem.gutkovskiy.financialaccounting.entity.Expense;
    import artem.gutkovskiy.financialaccounting.repository.ExpenseRepository;
    import artem.gutkovskiy.financialaccounting.cache.ExpenseCache;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class ExpenseService {
        private final ExpenseRepository expenseRepository;
        private final ExpenseCache expenseCache;

        public ExpenseService(ExpenseRepository expenseRepository, ExpenseCache expenseCache) {
            this.expenseRepository = expenseRepository;
            this.expenseCache = expenseCache;
        }

        public List<Expense> findAll() {
            return expenseRepository.findAll();
        }

        public Optional<Expense> findById(Long id) {
            return expenseRepository.findById(id);
        }

        public Expense save(Expense expense) {
            expenseCache.invalidate(expense.getUserName());
            return expenseRepository.save(expense);
        }

        public void deleteById(Long id) {
            expenseRepository.findById(id).ifPresent(expense -> {
                expenseCache.invalidate(expense.getUserName());
                expenseRepository.deleteById(id);
            });
        }

        public List<Expense> findExpensesByUserName(String userName) {
            if (expenseCache.containsKey(userName)) {
                return expenseCache.get(userName);
            } else {
                List<Expense> expenses = expenseRepository.findExpensesByUserName(userName);
                if (!expenses.isEmpty()) {
                    expenseCache.put(userName, expenses);
                }

                return expenses;
            }
        }
    }
