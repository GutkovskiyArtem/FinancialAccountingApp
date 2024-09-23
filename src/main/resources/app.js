document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('expenseForm');
    const expenseList = document.getElementById('expenseList');

    // Функция для отображения расходов
    const displayExpenses = (expenses) => {
        expenseList.innerHTML = '';
        expenses.forEach(expense => {
            const li = document.createElement('li');
            li.innerHTML = `${expense.description}: $${expense.amount}
                            <button onclick="deleteExpense(${expense.id})">Delete</button>`;
            expenseList.appendChild(li);
        });
    };

    // Загрузка расходов при загрузке страницы
    const loadExpenses = async () => {
        const response = await fetch('/api/expenses');
        const expenses = await response.json();
        displayExpenses(expenses);
    };

    // Добавление нового расхода
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const amount = document.getElementById('amount').value;
        const description = document.getElementById('description').value;

        const newExpense = { amount, description };

        await fetch('/api/expenses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newExpense)
        });

        form.reset();
        loadExpenses(); // Обновляем список
    });

    // Удаление расхода
    window.deleteExpense = async (id) => {
        await fetch(`/api/expenses/${id}`, {
            method: 'DELETE',
        });
        loadExpenses(); // Обновляем список
    };

    // Поиск расходов по имени пользователя
    document.getElementById('findExpensesForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const userName = document.getElementById('searchUser').value;

        const response = await fetch(`/api/expenses/by-username?userName=${userName}`);
        const expenses = await response.json();

        if (Array.isArray(expenses) && expenses.length > 0) {
            displayExpenses(expenses); // Отобразить расходы
        } else {
            expenseList.innerHTML = '<li>No expenses found for this user.</li>'; // Сообщение об отсутствии расходов
        }
    });

    // Загрузка расходов при первой загрузке
    loadExpenses();
});
