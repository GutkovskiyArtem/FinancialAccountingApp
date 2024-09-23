document.addEventListener('DOMContentLoaded', () => {
    const userForm = document.getElementById('userForm');
    const userSelect = document.getElementById('userSelect');
    const form = document.getElementById('expenseForm');
    const expenseList = document.getElementById('expenseList');

    async function loadUsers() {
        const response = await fetch('/api/users');
        const users = await response.json();
        userSelect.innerHTML = '';
        users.forEach(user => {
            const option = document.createElement('option');
            option.value = user.name;
            option.setAttribute('data-id', user.id);
            option.textContent = user.name;
            userSelect.appendChild(option);
        });
    }

    userForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const newUserName = document.getElementById('newUserName').value;

        const response = await fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: newUserName })
        });

        if (response.ok) {
            await loadUsers();
            document.getElementById('newUserName').value = '';
        }
    });

    document.getElementById('deleteUserButton').addEventListener('click', async () => {
        const selectedUserName = userSelect.value;
        const selectedUserId = parseInt(userSelect.options[userSelect.selectedIndex].getAttribute('data-id'));
        if (selectedUserName && confirm(`Вы уверены, что хотите удалить пользователя "${selectedUserName}"?`)) {
            const response = await fetch(`/api/users/${selectedUserId}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                await loadUsers();
                alert(`Пользователь "${selectedUserName}" удален.`);
            } else {
                alert('Ошибка при удалении пользователя.');
            }
        }
    });


    document.getElementById('loadExpenses').addEventListener('click', async () => {
        const selectedUserName = userSelect.value;
        await loadExpenses(selectedUserName);
    });


    async function loadExpenses(userName) {
        const response = await fetch(`/api/expenses/by-username?userName=${userName}`);
        const expenses = await response.json();
        displayExpenses(expenses);
    }


    const displayExpenses = (expenses) => {
        expenseList.innerHTML = '';
        expenses.forEach(expense => {
            const li = document.createElement('li');
            li.innerHTML = `${expense.description}: $${expense.amount}
                            <button onclick="deleteExpense(${expense.id})">Delete</button>
                            <button onclick="editExpense(${expense.id}, '${expense.description}', '${expense.amount}')">Edit</button>`;
            expenseList.appendChild(li);
        });
    };


    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const amount = document.getElementById('amount').value;
        const description = document.getElementById('description').value;
        const expenseUserName = userSelect.value;
        const expenseId = form.dataset.id;

        const expenseData = { amount, description, userName: expenseUserName };

        let response;
        if (expenseId) {
            response = await fetch(`/api/expenses/${expenseId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(expenseData)
            });
            delete form.dataset.id;
        } else {
            response = await fetch('/api/expenses', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(expenseData)
            });
        }

        if (response.ok) {
            form.reset();
            console.log('Расход успешно добавлен/обновлен');
            await loadExpenses(expenseUserName);
        } else {
            console.error('Ошибка при добавлении/обновлении расхода');
        }
    });

    window.deleteExpense = async (id) => {
        const userName = userSelect.value;
        await fetch(`/api/expenses/${id}`, {
            method: 'DELETE',
        });
        console.log('Расход успешно удален');
        await loadExpenses(userName);
    };

    window.editExpense = (id, description, amount) => {
        document.getElementById('amount').value = amount;
        document.getElementById('description').value = description;
        document.getElementById('expenseForm').dataset.id = id;
    };

    loadUsers();
});
