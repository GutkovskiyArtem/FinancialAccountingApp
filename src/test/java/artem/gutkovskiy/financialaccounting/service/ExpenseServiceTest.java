package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.entity.Expense;
import artem.gutkovskiy.financialaccounting.repository.ExpenseRepository;
import artem.gutkovskiy.financialaccounting.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {
    private ExpenseService expenseService;
    private ExpenseRepository expenseRepository;
    private Cache<Expense> expenseCache;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        expenseCache = mock(Cache.class);
        expenseService = new ExpenseService(expenseRepository, expenseCache);
    }

    @Test
    void findAll_shouldReturnAllExpenses() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Test Expense");
        expense.setAmount(100.0);

        when(expenseCache.containsKey("all_expenses")).thenReturn(false);
        when(expenseRepository.findAll()).thenReturn(Collections.singletonList(expense));
        doNothing().when(expenseCache).put(any(String.class), any());

        var result = expenseService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test Expense", result.get(0).getDescription());
        verify(expenseCache).put("all_expenses", Collections.singletonList(expense));
    }

    @Test
    void findAll_shouldReturnCachedExpenses_whenCacheExists() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Cached Expense");
        expense.setAmount(200.0);

        when(expenseCache.containsKey("all_expenses")).thenReturn(true);
        when(expenseCache.get("all_expenses")).thenReturn(Collections.singletonList(expense));

        var result = expenseService.findAll();

        assertEquals(1, result.size());
        assertEquals("Cached Expense", result.get(0).getDescription());
        verify(expenseRepository, never()).findAll();
    }

    @Test
    void findById_shouldReturnExpense_whenExists() {
        Expense expense = new Expense();
        expense.setId(1L);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        var result = expenseService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(expense, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        var result = expenseService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void save_shouldSaveExpense() {
        Expense expense = new Expense();
        expense.setDescription("Test Expense");
        expense.setUserName("User1");
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        var savedExpense = expenseService.save(expense);

        assertEquals(expense, savedExpense);
        verify(expenseCache).invalidate("all_expenses");
    }

    @Test
    void deleteById_shouldDeleteExpense_whenExists() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUserName("User1");

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        expenseService.deleteById(1L);
        verify(expenseCache).invalidate("all_expenses");
        verify(expenseCache).put("all_expenses", Collections.emptyList());
    }

    @Test
    void deleteById_shouldNotDelete_whenNotExists() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        expenseService.deleteById(1L);

        verify(expenseCache, never()).invalidate(anyString());
        verify(expenseRepository, never()).deleteById(anyLong());
    }

    @Test
    void findExpensesByUserName_shouldReturnCachedExpenses_whenCacheExists() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUserName("User1");

        when(expenseCache.containsKey("User1")).thenReturn(true);
        when(expenseCache.get("User1")).thenReturn(Collections.singletonList(expense));

        var result = expenseService.findExpensesByUserName("User1");

        assertEquals(1, result.size());
        assertEquals("User1", result.get(0).getUserName());
        verify(expenseRepository, never()).findExpensesByUserName(anyString());
    }

    @Test
    void findExpensesByUserName_shouldReturnExpensesFromDatabase_whenNotCached() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUserName("User1");

        when(expenseCache.containsKey("User1")).thenReturn(false);
        when(expenseRepository.findExpensesByUserName("User1")).thenReturn(Collections.singletonList(expense));
        doNothing().when(expenseCache).put(any(String.class), any());

        var result = expenseService.findExpensesByUserName("User1");

        assertEquals(1, result.size());
        assertEquals("User1", result.get(0).getUserName());
        verify(expenseCache).put("User1", Collections.singletonList(expense));
    }
    @Test
    void findById_shouldReturnCachedExpense_whenExistsInCache() {
        Expense expense = new Expense();
        expense.setId(1L);
        when(expenseCache.containsKey("1")).thenReturn(true);
        when(expenseCache.get("1")).thenReturn(Collections.singletonList(expense));

        var result = expenseService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(expense, result.get());
        verify(expenseRepository, never()).findById(anyLong());
    }

    @Test
    void findExpensesByUserName_shouldReturnEmpty_whenNoExpensesFound() {
        when(expenseCache.containsKey("User1")).thenReturn(false);
        when(expenseRepository.findExpensesByUserName("User1")).thenReturn(Collections.emptyList());

        var result = expenseService.findExpensesByUserName("User1");

        assertTrue(result.isEmpty());
        verify(expenseCache, never()).put(anyString(), any());
    }

}
