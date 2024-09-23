package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.dto.RequestObject;
import artem.gutkovskiy.financialaccounting.dto.ResponseObject;
import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulkService {
    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseService.class);
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    @Autowired
    public BulkService(ExpenseRepository expenseRepository,
                       UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    public List<ResponseObject> processExpenses(
            List<RequestObject> expenseRequests) {
        return expenseRequests.stream()
                .map(this::createExpenseFromRequest)
                .collect(Collectors.toList());
    }

    private ResponseObject createExpenseFromRequest(
            RequestObject requestObject) {
        logger.info("Поиск пользователя по ID: {}", requestObject.getUserId());
        User user = userService.findById(requestObject.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with ID: " +
                                requestObject.getUserId()));

        logger.info("Создание расхода");
        Expense expense = new Expense();
        expense.setDescription(requestObject.getDescription());
        expense.setAmount(requestObject.getAmount());
        expense.setUser(user);
        expense.setUserName(user.getName());
        logger.info("Сохранение расхода");
        Expense savedExpense = expenseRepository.save(expense);

        return new ResponseObject(savedExpense.getId(),
                "Expense created successfully");
    }
}
