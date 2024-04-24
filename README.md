# Expense Controller 

This project implements an Expense Controller  using the Spring Boot framework.

## Introduction

The Expense Controller API provides an endpoint to retrieve expenses by category for a specific year and month.

## Endpoints

- **Get Expenses by Category:**

  Retrieves expenses by category for a specific year and month.

  - Endpoint: `/expenses-by-category`
  - Method: GET
  - Parameters:
    - `year` (required): The year for which to retrieve expenses.
    - `month` (required): The month for which to retrieve expenses.
  - Example Request:
    ```http
    GET /expenses-by-category?year=2022&month=10
    ```
  - Example Response:
    ```json
    {
      "year": 2022,
      "month": 10,
      "expenses": [
        {
          "category": "Food",
          "amount": 500
        },
        {
          "category": "Transportation",
          "amount": 200
        },
        {
          "category": "Entertainment",
          "amount": 300
        }
      ]
    }
    ```

## Contributing

Contributions are welcome! If you find any issues or have improvements to suggest, feel free to open an issue or create a pull request.
