package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.dto.RequestObject;
import artem.gutkovskiy.financialaccounting.dto.ResponseObject;
import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BulkServiceTest {
    private BulkService bulkService;
    private ExpenseRepository expenseRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        userService = mock(UserService.class);
        bulkService = new BulkService(expenseRepository, userService);
    }

    @Test
    void processExpenses_shouldCreateExpensesSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        RequestObject requestObject = new RequestObject("Test Expense", 100.0, 1L);
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> {
            Expense expense = invocation.getArgument(0);
            expense.setId(1L);
            return expense;
        });

        ResponseObject response = bulkService.processExpenses(Arrays.asList(requestObject)).get(0);

        assertEquals(1L, response.getId());
        assertEquals("Expense created successfully", response.getMessage());

        verify(userService).findById(1L);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void processExpenses_shouldThrowException_whenUserNotFound() {
        RequestObject requestObject = new RequestObject("Test Expense", 100.0, 1L);
        when(userService.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bulkService.processExpenses(Arrays.asList(requestObject));
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }
}
